package indi.shui4.producer.web.handler;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author shui4
 */
@RestControllerAdvice
public class MyExceptionHandler {
  @ExceptionHandler(Exception.class)
  public String handle(Throwable e) {
    return "error:" + e.getMessage();
  }
}
