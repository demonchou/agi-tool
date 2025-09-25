package com.demonchou.agi.tool.pay.domain.model.trans;

import lombok.Data;

/**
 * Google 开发者后台发送的通知
 *
 * @author demonchou
 * @version GoogleDeveloperNotification, 2025/8/28 11:32 demonchou
 */
@Data
public class GoogleDeveloperNotification {
    /**
     * 版本
     */
    private String version;

    /**
     * 安卓包名
     */
    private String packageName;

    private String eventTimeMillis;

    /**
     * 一次性购买通知
     */
    private GoogleOneTimeProductNotification oneTimeProductNotification;

    /**
     * 购买作废通知
     */
    private GoogleVoidedPurchaseNotification voidedPurchaseNotification;

    /**
     * google 管理后台发送的测试通知
     */
    private GoogleTestNotification testNotification;
}
