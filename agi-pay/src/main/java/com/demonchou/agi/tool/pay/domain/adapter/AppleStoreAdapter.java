package com.demonchou.agi.tool.pay.domain.adapter;

import com.apple.itunes.storekit.model.JWSTransactionDecodedPayload;
import com.apple.itunes.storekit.model.ResponseBodyV2DecodedPayload;

/**
 * 苹果商店服务适配器
 *
 * @author demonchou
 * @version AppleStoreService, 2025/8/25 13:47 demonchou
 */
public interface AppleStoreAdapter {

    JWSTransactionDecodedPayload getApplePurchaseTransaction(String transactionId);

    ResponseBodyV2DecodedPayload getNotification(String signedPayload);

    JWSTransactionDecodedPayload verifyJWSTransaction(String signedPayload);
}
