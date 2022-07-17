package indi.shui4.producer.service;

import cn.hutool.core.bean.BeanUtil;
import indi.shui4.api.model.dto.OrderDTO;
import indi.shui4.producer.config.mq.TransactionalRabbitTemplate;
import indi.shui4.producer.mapper.OrderMapper;
import indi.shui4.producer.model.po.Order;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author shui4
 */
@Service
@AllArgsConstructor
public class OrderService {
  private TransactionalRabbitTemplate rabbitTemplate;
  private OrderMapper orderMapper;

  @Transactional(rollbackFor = Exception.class)
  public void add(Order order) {
    orderMapper.insert(order);
    OrderDTO dto = BeanUtil.copyProperties(order, OrderDTO.class);
    rabbitTemplate.sendTransactionalMsg("order.exchange", "rk", dto);
  }

  @Transactional(rollbackFor = Exception.class)
  public void edit(Order order) {
    orderMapper.updateByPrimaryKeySelective(order);
    OrderDTO dto = BeanUtil.copyProperties(order, OrderDTO.class);
    rabbitTemplate.sendTransactionalUpdateMsg("order.exchange", "rk", dto);
  }
}
