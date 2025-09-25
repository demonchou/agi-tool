package com.demonchou.agi.tool.pay.domain.model.trans;

import lombok.Data;

/**
 * 谷歌通知参数
 *
 * @author demonchou
 * @version GoogleNotification, 2025/8/28 11:41 demonchou
 */
@Data
public class GoogleNotification {
    public static final int SUBSCRIPTION_RECOVERED = 1;

    public static final int SUBSCRIPTION_RENEWED = 2;

    public static final int SUBSCRIPTION_CANCELED = 3;

    public static final int SUBSCRIPTION_PURCHASED = 4;

    public static final int SUBSCRIPTION_ON_HOLD = 5;

    public static final int SUBSCRIPTION_IN_GRACE_PERIOD = 6;

    public static final int SUBSCRIPTION_RESTARTED = 7;

    public static final int SUBSCRIPTION_PRICE_CHANGE_CONFIRMED = 8;

    public static final int SUBSCRIPTION_DEFERRED = 9;

    public static final int SUBSCRIPTION_PAUSED = 10;

    public static final int SUBSCRIPTION_PAUSE_SCHEDULE_CHANGED = 11;

    public static final int SUBSCRIPTION_REVOKED = 12;

    public static final int SUBSCRIPTION_EXPIRED = 13;

    public static final int ONE_TIME_PRODUCT_PURCHASED = 1;

    public static final int ONE_TIME_PRODUCT_CANCELED = 2;

    /**
     * 通知版本
     */
    private String version;

    /**
     * 通知类型
     * 订阅通知参数值:
     * (1) SUBSCRIPTION_RECOVERED - A subscription was recovered from account hold.
     * (2) SUBSCRIPTION_RENEWED - An active subscription was renewed.
     * (3) SUBSCRIPTION_CANCELED - A subscription was either voluntarily or involuntarily cancelled. For voluntary cancellation, sent when the user cancels.
     * (4) SUBSCRIPTION_PURCHASED - A new subscription was purchased.
     * (5) SUBSCRIPTION_ON_HOLD - A subscription has entered account hold (if enabled).
     * (6) SUBSCRIPTION_IN_GRACE_PERIOD - A subscription has entered grace period (if enabled).
     * (7) SUBSCRIPTION_RESTARTED - User has restored their subscription from Play > Account > Subscriptions. The subscription was canceled but had not expired yet when the user restores. For more information, see Restorations.
     * (8) SUBSCRIPTION_PRICE_CHANGE_CONFIRMED - A subscription price change has successfully been confirmed by the user.
     * (9) SUBSCRIPTION_DEFERRED - A subscription's recurrence time has been extended.
     * (10) SUBSCRIPTION_PAUSED - A subscription has been paused.
     * (11) SUBSCRIPTION_PAUSE_SCHEDULE_CHANGED - A subscription pause schedule has been changed.
     * (12) SUBSCRIPTION_REVOKED - A subscription has been revoked from the user before the expiration time.
     * (13) SUBSCRIPTION_EXPIRED - A subscription has expired.
     * (20) SUBSCRIPTION_PENDING_PURCHASE_CANCELED - A pending transaction of a subscription has been canceled.
     * 一次性商品通知参数值:
     * (1) ONE_TIME_PRODUCT_PURCHASED - A one-time product was successfully purchased by a user.
     * (2) ONE_TIME_PRODUCT_CANCELED - A pending one-time product purchase has been canceled by the user.
     */
    private Integer notificationType;

    /**
     * 购买凭证
     */
    private String purchaseToken;
}
