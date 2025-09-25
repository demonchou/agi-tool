package com.demonchou.agi.tool.pay.infra.persistence.repository.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.demonchou.agi.tool.pay.domain.enums.OrderStatus;
import com.demonchou.agi.tool.pay.domain.model.order.Order;
import com.demonchou.agi.tool.pay.domain.model.order.OrderItem;
import com.demonchou.agi.tool.pay.domain.repository.OrderRepository;
import com.demonchou.agi.tool.pay.infra.converter.OrderPOConverter;
import com.demonchou.agi.tool.pay.infra.persistence.entity.OrderPO;
import com.demonchou.agi.tool.pay.infra.persistence.mapper.OrderMapper;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

/**
 * 订单仓储实现类
 *
 * @author demonchou
 * @version OrderRepositoryImpl, 2025/8/22 16:44 demonchou
 */
@Repository
public class OrderRepositoryImpl implements OrderRepository {
    @Resource
    private OrderMapper orderMapper;

    @Override
    public Long save(Order order) {
        OrderPO po = OrderPOConverter.toPO(order);
        orderMapper.insert(po);
        return po.getId();
    }

    @Override
    public void update(Order order) {
        OrderPO po = OrderPOConverter.toPO(order);
        orderMapper.updateById(po);
    }

    @Override
    public Order queryByPayRequestId(String payRequestId) {
        LambdaQueryWrapper<OrderPO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(OrderPO::getPayRequestId, payRequestId);
        OrderPO orderPO = orderMapper.selectOne(queryWrapper);

        if (null == orderPO) {
            return null;
        }

        OrderItem orderItem = new OrderItem();
        orderItem.setPlatformProductId(orderPO.getPlatformProductId());
        orderItem.setPayRequestId(orderPO.getPayRequestId());
        return new Order(orderPO.getId(), orderPO.getUserId(), OrderStatus.valueOf(orderPO.getStatus()), orderItem);
    }
}
