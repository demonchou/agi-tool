package com.demonchou.agi.tool.pay.domain.model.order;

import com.alibaba.fastjson2.JSON;
import com.demonchou.agi.tool.pay.domain.enums.OrderStatus;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

/**
 * 订单领域
 *
 * @author demonchou
 * @version Order, 2025/8/21 14:53 demonchou
 */
@Data
public class Order {
    private Long id;
    private Long userId;
    private OrderStatus status;
    private OrderItem orderItem;

    public boolean isInit() {
        return this.status == OrderStatus.INIT;
    }

    public boolean isPaid() {
        return this.status == OrderStatus.PAID;
    }

    public Order() {
    }

    public Order(Long id, Long userId, OrderStatus status, OrderItem orderItem) {
        this.id = id;
        this.userId = userId;
        this.orderItem = orderItem;
        this.status = status;
    }

    public Order(Long id, OrderStatus status, OrderItem orderItem) {
        this.id = id;
        this.status = status;
        this.orderItem = orderItem;
    }

    // 创建订单
    public static Order createOrder(CreateOrderParam createOrderParam) {
        OrderItem orderItem = buildOrderItem(createOrderParam);
        return new Order(null, createOrderParam.getUserId(), OrderStatus.INIT, orderItem);
    }

    public static Order updateOrderForConfirm(Long id, String status, String platformOrderId, Long payDate, Long payAmount,
                                              String currency) {
        Assert.notNull(id, "订单id不能为空");
        Assert.notNull(platformOrderId, "平台订单id不能为空");
        OrderItem orderItem = new OrderItem();
        orderItem.setPlatformOrderId(platformOrderId);
        orderItem.setPayDate(payDate);

        if (payAmount != null) {
            orderItem.setPayAmount(payAmount);
        }
        if (StringUtils.isNotBlank(currency)) {
            orderItem.setCurrency(currency);
        }
        return new Order(id, OrderStatus.valueOf(status), orderItem);
    }

    private static OrderItem buildOrderItem(CreateOrderParam createOrderParam) {
        OrderItem orderItem = new OrderItem();
        orderItem.setPayChannel(createOrderParam.getPayChannel());
        orderItem.setGoodsId(createOrderParam.getGoodsId());
        orderItem.setGoodsName(createOrderParam.getGoodsName());
        orderItem.setPrice(createOrderParam.getPrice());
        orderItem.setPayAmount(createOrderParam.getPayAmount());
        orderItem.setCurrency(createOrderParam.getCurrency());
        orderItem.setPayRequestId(createOrderParam.getPayRequestId());
        orderItem.setPlatformProductId(createOrderParam.getPlatformProductId());
        orderItem.setAdditionalInfo(JSON.toJSONString(createOrderParam.getAdditionalInfo()));
        return orderItem;
    }
}
