package com.demonchou.agi.tool.pay.domain.model.trans;

import lombok.Data;

/**
 * google支付订单取消通知
 *
 * @author demonchou
 * @version GoogleVoidedPurchaseNotification, 2025/8/28 11:42 demonchou
 */
@Data
public class GoogleVoidedPurchaseNotification {
    /**
     * 购买凭证
     */
    private String purchaseToken;

    /**
     * The unique order ID associated with the transaction that has been voided. For one time purchases,
     * this represents the only order ID generated for the purchase. For auto-renewing subscriptions,
     * a new order ID is generated for each renewal transaction.
     */
    private String orderId;

    /**
     * The productType for a voided purchase can have the following values:
     * <p>
     * (1) PRODUCT_TYPE_SUBSCRIPTION - A subscription purchase has been voided.
     * (2) PRODUCT_TYPE_ONE_TIME - A one-time purchase has been voided.
     */
    private Integer productType;

    /**
     * The refundType for a voided purchase can have the following values:
     * <p>
     * (1) REFUND_TYPE_FULL_REFUND - The purchase has been fully voided.
     * (2) REFUND_TYPE_QUANTITY_BASED_PARTIAL_REFUND - The purchase has been partially voided
     * by a quantity-based partial refund, applicable only to multi-quantity purchases. A purchase can be partially voided multiple times.
     * <p>
     * Note when the remaining total quantity of a multi-quantity purchase is refunded, the refundType will be REFUND_TYPE_FULL_REFUND.
     */
    private Integer refundType;
}
