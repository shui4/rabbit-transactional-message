package indi.shui4.api.model.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Optional;

/**
 * @author shui4
 */
@Data
@Accessors(chain = true)
public class OrderDTO implements Serializable, IMessage {

  private Long id;
  private String content;

  @Override
  public String getBusinessKey() {
    return Optional.ofNullable(id).map(Object::toString).orElse(null);
  }
}
