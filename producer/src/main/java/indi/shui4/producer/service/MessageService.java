package indi.shui4.producer.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import indi.shui4.api.model.dto.IMessage;
import indi.shui4.producer.mapper.MessageContentMapper;
import indi.shui4.producer.mapper.MessageMapper;
import indi.shui4.producer.model.po.Message;
import indi.shui4.producer.model.po.MessageContent;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

/**
 * @author shui4
 */
@Service
@AllArgsConstructor
public class MessageService {

  public static final int READY = 0;
  public static final int SUCCESS = 1;
  public static final int FAIL = 2;
  public static final List<Integer> TS = Arrays.asList(READY, FAIL);
  private ObjectMapper objectMapper;

  private MessageMapper messageMapper;
  private MessageContentMapper messageContentMapper;

  public static void main(String[] args) {
    System.out.println(LocalDateTime.now().plusHours(2));
  }

  @NonNull
  public Long save(String exchange, String routingKey, IMessage businessMsg, boolean updateOpt) {
    Message message =
        new Message()
            .setExchange(exchange)
            .setRoutingKey(routingKey)
            .setBusinessKey(businessMsg.getBusinessKey())
            .setStatus(READY)
            .setCreateTime(LocalDateTime.now());
    if (updateOpt) {
      message.setOperate(2);
    }
    messageMapper.insert(message);
    try {
      Long id = message.getId();
      MessageContent record =
          new MessageContent().setMsgId(id).setMsg(objectMapper.writeValueAsString(businessMsg));
      messageContentMapper.insert(record);
      return id;
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }

  public void success(Long msgId) {
    messageMapper.updateByPrimaryKeySelective(new Message().setId(msgId).setStatus(SUCCESS));
  }

  public void fail(Long msgId) {
    messageMapper.updateByPrimaryKeySelective(new Message().setId(msgId).setStatus(FAIL));
  }

  public List<Message> findUnsuccessfulForList() {
    LocalDateTime time = LocalDateTime.now().plusSeconds(-30);
    return messageMapper.findList(TS, time);
  }

  public LocalDateTime selectSuccessfulMaxCreateTime(Message businessKey) {
    return messageMapper.selectSuccessfulMaxCreateTime(businessKey);
  }
}
