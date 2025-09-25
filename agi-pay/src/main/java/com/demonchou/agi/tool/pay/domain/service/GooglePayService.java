package com.demonchou.agi.tool.pay.domain.service;

import com.demonchou.agi.tool.pay.domain.model.trans.GoogleDeveloperNotification;

/**
 * google支付领域服务
 *
 * @author demonchou
 * @version GoogleService, 2025/8/21 16:52 demonchou
 */
public interface GooglePayService {
    /**
     * 确认购买
     */
    boolean confirmPurchase(String platformProductId, String purchaseToken);

    void processOneTimeProductNotification(GoogleDeveloperNotification notification);

}
