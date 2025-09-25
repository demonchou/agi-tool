package com.demonchou.agi.tool.pay.domain.service;

/**
 * 苹果支付领域服务
 *
 * @author demonchou
 * @version AppleService, 2025/8/21 14:52 demonchou
 */
public interface ApplePayService {
    /**
     * 确认购买
     */
    boolean confirmPurchase(String transactionId);

    /**
     * 处理通知
     *
     * @param payload 通知内容
     * @return
     */
    boolean processNotification(String payload);
}
