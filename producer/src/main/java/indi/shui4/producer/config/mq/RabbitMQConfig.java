package indi.shui4.producer.config.mq;

import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author shui4
 */
@Configuration
@AutoConfigureBefore(RabbitAutoConfiguration.class)
public class RabbitMQConfig {

    @Bean
    public TransactionalRabbitTemplate transactionalRabbitTemplate() {
        return new TransactionalRabbitTemplate();
    }

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
