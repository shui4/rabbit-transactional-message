package indi.shui4.producer.scheduler;

import indi.shui4.producer.config.mq.TransactionalRabbitTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author shui4
 */
@Component
@Slf4j
public class MessageScheduler {
  @Autowired private TransactionalRabbitTemplate transactionalRabbitTemplate;

  @Scheduled(fixedRate = 5_000)
  public void retry() {
    transactionalRabbitTemplate.retry();
  }
}
