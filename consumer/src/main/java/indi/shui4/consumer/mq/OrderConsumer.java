package indi.shui4.consumer.mq;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

/**
 * @author shui4
 */
@Component
@Slf4j
@AllArgsConstructor
public class OrderConsumer {

  private JdbcTemplate jdbcTemplate;

  @RabbitListener(
      bindings = {
        @QueueBinding(
            value = @Queue(value = "order.queue"),
            exchange = @Exchange("order.exchange"),
            key = "rk")
      })
  public void receive(String dto) {
    log.info(dto.toString());
  }
}
