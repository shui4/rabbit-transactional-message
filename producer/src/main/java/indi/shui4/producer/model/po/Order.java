package indi.shui4.producer.model.po;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author shui4
 */
@Data
@Accessors(chain = true)
public class Order {
  private Long id;

  private String content;
}
