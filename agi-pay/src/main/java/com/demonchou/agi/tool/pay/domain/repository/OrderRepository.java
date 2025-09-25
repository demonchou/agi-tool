package com.demonchou.agi.tool.pay.domain.repository;

import com.demonchou.agi.tool.pay.domain.model.order.Order;

/**
 * 订单仓储接口
 *
 * @author demonchou
 * @version OrderRepository, 2025/8/21 17:42 demonchou
 */
public interface OrderRepository {

    Long save(Order order);

    void update(Order order);

    Order queryByPayRequestId(String payRequestId);
}
