package com.demonchou.agi.tool.pay.domain.service.impl;

import com.alibaba.fastjson2.JSON;
import com.google.api.services.androidpublisher.model.ProductPurchase;
import com.demonchou.agi.tool.pay.domain.adapter.GooglePlayAdapter;
import com.demonchou.agi.tool.pay.domain.enums.GoogleOneTimePurchaseStatus;
import com.demonchou.agi.tool.pay.domain.enums.OrderStatus;
import com.demonchou.agi.tool.pay.domain.model.order.Order;
import com.demonchou.agi.tool.pay.domain.model.trans.GoogleDeveloperNotification;
import com.demonchou.agi.tool.pay.domain.model.trans.GoogleNotification;
import com.demonchou.agi.tool.pay.domain.model.trans.GoogleOneTimeProductNotification;
import com.demonchou.agi.tool.pay.domain.repository.OrderRepository;
import com.demonchou.agi.tool.pay.domain.service.GooglePayService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * Google Play 支付服务实现
 *
 * @author demonchou
 * @version GoogleServiceImpl, 2025/8/27 10:01 demonchou
 */
@Slf4j
@Service
public class GooglePayServiceImpl implements GooglePayService {
    @Autowired
    private GooglePlayAdapter googlePlayAdapter;
    @Autowired
    private OrderRepository orderRepository;

    @Override
    public boolean confirmPurchase(String platformProductId, String purchaseToken) {
        ProductPurchase purchase = googlePlayAdapter.getProductPurchase(purchaseToken, platformProductId);
        if (purchase == null) {
            log.error("购买记录不存在, platformProductId:{}, purchaseToken:{}", platformProductId, purchaseToken);
            return false;
        }
        String obfuscatedExternalProfileId = purchase.getObfuscatedExternalProfileId();
        String obfuscatedExternalAccountId = purchase.getObfuscatedExternalAccountId();
        // 两者之一有值即可
        String payRequestId = StringUtils.isNotBlank(obfuscatedExternalAccountId) ? obfuscatedExternalAccountId : obfuscatedExternalProfileId;
        if (StringUtils.isBlank(payRequestId)) {
            log.error("google confirm purchase payRequestId id null, platformProductId:{}, purchaseToken:{}",
                    platformProductId, purchaseToken);
            return false;
        }

        if (!Objects.equals(purchase.getPurchaseState(), GoogleOneTimePurchaseStatus.PURCHASED)) {
            log.error("google confirm purchase，purchaseStatus not PURCHASED, platformProductId:{}, purchaseToken:{}",
                    platformProductId, purchaseToken);
            return false;
        }

        Order initialOrder = orderRepository.queryByPayRequestId(payRequestId);
        if (initialOrder == null) {
            log.error("订单不存在, payRequestId:{}", payRequestId);
            return false;
        }

        if (!initialOrder.isInit()) {
            log.error("订单状态不正确，不处理！, orderId:{}, status:{}", initialOrder.getId(), initialOrder.getStatus());
            return false;
        }

        if (Objects.equals(purchase.getConsumptionState(), 0)) {
            googlePlayAdapter.doConsumeInAppProductPurchase(purchaseToken, platformProductId);
        }
        // 更新订单状态及参数
        Order order = Order.updateOrderForConfirm(initialOrder.getId(), OrderStatus.PAID.getCode(), purchase.getOrderId(),
                purchase.getPurchaseTimeMillis() / 1000, null, null);
        orderRepository.update(order);
        return true;
    }

    @Override
    public void processOneTimeProductNotification(GoogleDeveloperNotification googleDeveloperNotification) {
        log.info("processOneTimeProductNotification: {}", JSON.toJSONString(googleDeveloperNotification));

        GoogleOneTimeProductNotification notification = googleDeveloperNotification.getOneTimeProductNotification();
        if (!Objects.equals(notification.getNotificationType(), GoogleNotification.ONE_TIME_PRODUCT_PURCHASED)) {
            log.warn("not one time product purchase notification, notificationType:{}", notification.getNotificationType());
            return;
        }

        String platformProductId = notification.getSku();
        ProductPurchase purchase = googlePlayAdapter.getProductPurchase(notification.getPurchaseToken(), platformProductId);
        if (purchase == null) {
            log.warn("has no purchase info, purchaseToken:{}, platformProductId:{}", notification.getPurchaseToken(), platformProductId);
            return;
        }

        String payRequestId = purchase.getObfuscatedExternalProfileId();

        if (StringUtils.isBlank(payRequestId)) {
            log.warn("payRequestId is null, purchaseToken:{}, platformProductId:{}", notification.getPurchaseToken(), platformProductId);
            return;
        }

        Order existOrder = orderRepository.queryByPayRequestId(payRequestId);
        if (existOrder == null) {
            log.warn("订单不存在, payRequestId:{}", payRequestId);
            return;
        }
        if (existOrder.isPaid()) {
            log.warn("订单已支付, orderId:{}, status:{}", existOrder.getId(), existOrder.getStatus());
            return;
        }

        String platformProductIdInOrder = existOrder.getOrderItem().getPlatformProductId();

        if (!Objects.equals(platformProductIdInOrder, platformProductId)) {
            log.warn("订单与通知平台产品id不匹配, orderId:{}, platformProductIdInOrder:{}, platformProductId:{}",
                    existOrder.getId(), platformProductIdInOrder, platformProductId);
            return;
        }

        confirmPurchase(platformProductId, notification.getPurchaseToken());

        log.info("process google one time notification success, notificationType:{}", GoogleNotification.ONE_TIME_PRODUCT_PURCHASED);
    }
}
