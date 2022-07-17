package indi.shui4.producer.web.controller;

import indi.shui4.producer.model.po.Order;
import indi.shui4.producer.service.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * @author shui4
 */
@RestController
@AllArgsConstructor
@RequestMapping("/order")
public class OrderController {

  private OrderService orderService;

  @PostMapping
  public String add(@RequestBody Order order) {
    try {
      orderService.add(order);
    } catch (RuntimeException e) {
    }
    return order.getId().toString();
  }

  @PutMapping
  public String edit(@RequestBody Order order) {
    try {
      orderService.edit(order);
    } catch (Exception e) {
      return e.getMessage();
    }
    return "success";
  }
}
