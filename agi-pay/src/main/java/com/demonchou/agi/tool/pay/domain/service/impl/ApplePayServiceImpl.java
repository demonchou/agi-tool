package com.demonchou.agi.tool.pay.domain.service.impl;

import com.alibaba.fastjson2.JSON;
import com.apple.itunes.storekit.model.Data;
import com.apple.itunes.storekit.model.JWSTransactionDecodedPayload;
import com.apple.itunes.storekit.model.ResponseBodyV2DecodedPayload;
import com.demonchou.agi.tool.pay.domain.adapter.AppleStoreAdapter;
import com.demonchou.agi.tool.pay.domain.enums.OrderStatus;
import com.demonchou.agi.tool.pay.domain.model.order.Order;
import com.demonchou.agi.tool.pay.domain.model.order.OrderItem;
import com.demonchou.agi.tool.pay.domain.repository.OrderRepository;
import com.demonchou.agi.tool.pay.domain.service.ApplePayService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static com.apple.itunes.storekit.model.Type.CONSUMABLE;

/**
 * Apple Pay 支付服务实现
 *
 * @author demonchou
 * @version AppleServiceImpl, 2025/8/27 10:00 demonchou
 */
@Slf4j
@Service
public class ApplePayServiceImpl implements ApplePayService {
    @Autowired
    private AppleStoreAdapter appleStoreAdapter;
    @Autowired
    private OrderRepository orderRepository;

    @Override
    public boolean confirmPurchase(String transactionId) {
        JWSTransactionDecodedPayload transaction = appleStoreAdapter.getApplePurchaseTransaction(transactionId);
        if (transaction == null) {
            log.error("can not get transaction info, transactionId:{}", transactionId);
            return false;
        }
        UUID appAccountToken = transaction.getAppAccountToken();
        if (appAccountToken == null) {
            log.error("can not get appAccountToken(payRequestId), transactionId:{}", transactionId);
            return false;
        }

        Order initialOrder = orderRepository.queryByPayRequestId(appAccountToken.toString());
        if (initialOrder == null) {
            log.error("order not found, payRequestId:{}", appAccountToken);
            return false;
        }

        OrderItem orderItem = initialOrder.getOrderItem();

        if (!initialOrder.isInit()) {
            log.error("order status not init，skip confirmPurchase, orderId:{}, status:{}", initialOrder.getId(), initialOrder.getStatus());
            return false;
        }

        String productId = transaction.getProductId();
        if (!productId.equalsIgnoreCase(orderItem.getPlatformProductId())) {
            log.error("product id not match, transactionId:{}, productId:{}", transactionId, productId);
            return false;
        }

        // 更新订单状态及参数
        Order order = Order.updateOrderForConfirm(initialOrder.getId(), OrderStatus.PAID.getCode(), transactionId,
                transaction.getPurchaseDate(), getLocalizedPrice(transaction), transaction.getCurrency());
        orderRepository.update(order);
        return true;
    }

    @Override
    public boolean processNotification(String notifyPayload) {
        ResponseBodyV2DecodedPayload notification = appleStoreAdapter.getNotification(notifyPayload);
        if (notification == null) {
            log.error("[apple notification process] getNotification failed, notifyPayload: {}", notifyPayload);
            return false;
        }

        Data data = notification.getData();
        if (data == null) {
            log.error("[apple notification process] getData failed, notifyPayload: {}", notifyPayload);
            return false;
        }

        JWSTransactionDecodedPayload transaction = appleStoreAdapter.verifyJWSTransaction(data.getSignedTransactionInfo());
        if (transaction == null) {
            log.error("[apple notification process] verifyJWSTransaction failed, data: {}", JSON.toJSONString(data));
            return false;
        }

        UUID uuid = transaction.getAppAccountToken();
        if (uuid == null) {
            log.error("[apple notification process] getAppAccountToken failed data:{}", JSON.toJSONString(data));
            return false;
        }

        if (transaction.getType() == CONSUMABLE) {
            Order existOrder = orderRepository.queryByPayRequestId(uuid.toString());
            if (existOrder == null) {
                log.error("[apple notification process] order not found, payRequestId:{}", uuid);
                return false;
            }
            this.confirmPurchase(transaction.getTransactionId());
        } else {
            log.error("[apple notification process] not support type:{}", transaction.getType());
        }
        return true;
    }

    private Long getLocalizedPrice(JWSTransactionDecodedPayload transaction) {
        if (transaction == null || transaction.getPrice() == null) {
            return null;
        }

        return transaction.getPrice() / 10;
    }
}
