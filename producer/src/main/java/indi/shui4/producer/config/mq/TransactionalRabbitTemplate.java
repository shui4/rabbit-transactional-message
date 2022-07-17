package indi.shui4.producer.config.mq;

import cn.hutool.core.lang.UUID;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import indi.shui4.api.model.dto.IMessage;
import indi.shui4.api.model.dto.OrderDTO;
import indi.shui4.producer.model.po.Message;
import indi.shui4.producer.service.MessageService;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 保证事物提交成功，消息一定会发送出去。 <br>
 *
 * <p>注意：Consumer必须通过Str接收（无法使用Spring Boot封装的Convert，以对象作为参数）
 *
 * @author shui4
 */
@Slf4j
public class TransactionalRabbitTemplate {
  public static final Integer UPDATE_OPT = 2;
  @Autowired private RabbitTemplate rabbitTemplate;

  @Autowired private MessageService messageService;
  @Autowired private ObjectMapper objectMapper;
  @Autowired private RedissonClient redissonClient;

  @PostConstruct
  public void init() {
    rabbitTemplate.setChannelTransacted(true);
  }

  public void sendTransactionalMsg(String exchange, String routingKey, final IMessage object)
      throws AmqpException {
    sendTransactionalMsg(exchange, routingKey, object, false);
  }

  public void sendTransactionalUpdateMsg(String exchange, String routingKey, final IMessage object)
      throws AmqpException {
    sendTransactionalMsg(exchange, routingKey, object, true);
  }

  public void sendTransactionalMsg(
      String exchange, String routingKey, final IMessage object, boolean update)
      throws AmqpException {
    String jsonStr;
    try {
      jsonStr = objectMapper.writeValueAsString(object);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
    Long msgId = messageService.save(exchange, routingKey, object, update);
    String finalJsonStr = jsonStr;
    TransactionSynchronizationManager.registerSynchronization(
        new TransactionSynchronization() {
          @Override
          public void afterCommit() {
            throwExceptionByTest(object);
            RLock lock = null;
            // 只有下游是更新操作的时候，才考虑加锁，目的是预防 A（error）B（success）A（retry）问题
            if (update) {
              lock = redissonClient.getLock(object.getBusinessKey());
            }
            try {
              if (update) {
                lock.lock();
              }
              rabbitTemplate.send(
                  exchange,
                  routingKey,
                  new org.springframework.amqp.core.Message(finalJsonStr.getBytes()),
                  new CorrelationData(UUID.randomUUID().toString()));
              success(msgId);
            } catch (AmqpException e) {
              fail(msgId);
              log.error(
                  "exchange  -> {}，routingKey  -> {}，消息发送失败  -> {},",
                  exchange,
                  routingKey,
                  e.getMessage());
            } finally {
              if (update) {
                lock.unlock();
              }
            }
          }
        });
  }

  public void retry() throws AmqpException {
    List<Message> unsuccessfulList = messageService.findUnsuccessfulForList();
    for (Message message : unsuccessfulList) {
      // UPDATE  A（error）-B（success）-A（retry）
      if (UPDATE_OPT.equals(message.getOperate())) {
        RLock lock = redissonClient.getLock(message.getBusinessKey());
        try {
          if (!lock.tryLock()) {
            success(message.getId());
            return;
          }
          LocalDateTime maxDateTime = messageService.selectSuccessfulMaxCreateTime(message);
          if (maxDateTime != null && maxDateTime.compareTo(message.getCreateTime()) > 0) {
            success(message.getId());
            return;
          }
          send(message);
        } finally {
          lock.unlock();
        }
        return;
      }
      send(message);
    }
  }

  private void send(Message message) {
    Long msgId = message.getId();
    String exchange = message.getExchange();
    String routingKey = message.getRoutingKey();
    String msgContent = message.getMsgContent();
    try {
      rabbitTemplate.send(
          exchange, routingKey, new org.springframework.amqp.core.Message(msgContent.getBytes()));
      success(msgId);
    } catch (AmqpException e) {
      fail(msgId);
      log.error(
          "exchange  -> {}，routingKey  -> {}，消息发送失败  -> {},", exchange, routingKey, e.getMessage());
    }
  }

  private void success(Long msgId) {
    messageService.success(msgId);
  }

  private void fail(Long msgId) {
    messageService.fail(msgId);
  }

  private void throwExceptionByTest(IMessage object) {
    if (((OrderDTO) object).getContent().equals("A")) {
      throw new RuntimeException("故意A");
    }
  }
}
