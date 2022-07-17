package indi.shui4.producer.model.po;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * @author shui4
 */

/** 事物消息 */
@Data
@Accessors(chain = true)
public class Message {
  private Long id;

  private String exchange;

  private String routingKey;

  /** 消息状态（0-就绪，1-成功，2-失败） */
  private Integer status;

  /** 业务KEY,唯一的 */
  private String businessKey;

  private LocalDateTime createTime;

  private LocalDateTime updateTime;

  private Integer operate;

  private String msgContent;
}
