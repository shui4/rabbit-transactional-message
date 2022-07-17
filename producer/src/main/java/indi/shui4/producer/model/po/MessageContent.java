package indi.shui4.producer.model.po;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author shui4
 */
/**
    * 消息内容
    */
@Data
@Accessors(chain = true)
public class MessageContent {
    private Long id;

    /**
    * t_message.id
    */
    private Long msgId;

    /**
    * 消息
    */
    private String msg;
}