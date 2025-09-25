package com.demonchou.agi.tool.pay.domain.adapter;

import com.google.api.services.androidpublisher.AndroidPublisherScopes;
import com.google.api.services.androidpublisher.model.ProductPurchase;

import java.util.List;

/**
 * Google-play 接口适配器
 *
 * @author demonchou
 * @version GooglePlayService, 2025/8/26 14:48 demonchou
 */
public interface GooglePlayAdapter {

    List<String> SCOPES = List.of(AndroidPublisherScopes.ANDROIDPUBLISHER);

    ProductPurchase getProductPurchase(String purchaseToken, String platformProductId);

    void doConsumeInAppProductPurchase(String purchaseToken, String platformProductId);
}
