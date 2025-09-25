package com.demonchou.agi.tool.pay.infra.converter;

import com.demonchou.agi.tool.pay.domain.enums.OrderStatus;
import com.demonchou.agi.tool.pay.domain.model.order.Order;
import com.demonchou.agi.tool.pay.domain.model.order.OrderItem;
import com.demonchou.agi.tool.pay.infra.persistence.entity.OrderPO;

/**
 * OrderPOConverter
 *
 * @author demonchou
 * @version OrderPOConverter, 2025/9/2 14:57 demonchou
 */
public class OrderPOConverter {
    public static Order toDomain(OrderPO po) {
        if (po == null) {
            return null;
        }

        OrderItem orderItem = new OrderItem();
        orderItem.setGoodsId(po.getGoodsId());
        orderItem.setGoodsName(po.getGoodsName());
        orderItem.setPrice(po.getPrice());
        orderItem.setPayChannel(po.getPayChannel());
        orderItem.setPayRequestId(po.getPayRequestId());
        orderItem.setPlatformProductId(po.getPlatformProductId());
        orderItem.setPlatformOrderId(po.getPlatformOrderId());
        orderItem.setPayAmount(po.getPayAmount());
        orderItem.setConversionScale(po.getConversionScale());
        orderItem.setCurrency(po.getCurrency());
        orderItem.setPayDate(po.getPayDate());
        orderItem.setRefundId(po.getRefundId());
        return new Order(po.getId(), po.getUserId(), OrderStatus.valueOf(po.getStatus()), orderItem);
    }

    public static OrderPO toPO(Order order) {
        if (order == null) {
            return null;
        }

        OrderPO po = new OrderPO();
        OrderItem orderItem = order.getOrderItem();
        po.setId(order.getId());
        po.setUserId(order.getUserId());
        po.setStatus(order.getStatus().getCode());
        po.setGoodsId(orderItem.getGoodsId());
        po.setGoodsName(orderItem.getGoodsName());
        po.setPrice(orderItem.getPrice());
        po.setPayChannel(orderItem.getPayChannel());
        po.setPayRequestId(orderItem.getPayRequestId());
        po.setPlatformProductId(orderItem.getPlatformProductId());
        po.setPlatformOrderId(orderItem.getPlatformOrderId());
        po.setPayAmount(orderItem.getPayAmount());
        po.setConversionScale(orderItem.getConversionScale());
        po.setCurrency(orderItem.getCurrency());
        po.setPayDate(orderItem.getPayDate());
        po.setRefundId(orderItem.getRefundId());
        return po;
    }
}
