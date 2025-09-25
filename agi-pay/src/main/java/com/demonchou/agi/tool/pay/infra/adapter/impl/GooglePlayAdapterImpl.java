package com.demonchou.agi.tool.pay.infra.adapter.impl;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.androidpublisher.AndroidPublisher;
import com.google.api.services.androidpublisher.model.ProductPurchase;
import com.demonchou.agi.tool.pay.domain.adapter.GooglePlayAdapter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.Objects;

/**
 * Google Play Adapter
 *
 * @author demonchou
 * @version GooglePlayAdapterImpl, 2025/8/31 21:29 demonchou
 */
@Slf4j
@Service
public class GooglePlayAdapterImpl implements GooglePlayAdapter {

    private final AndroidPublisher.Purchases purchases;

    private final String packageName;

    public GooglePlayAdapterImpl(String proxyAddress, Integer port, String googlePlayPackageName, String serviceAccountFileName) {
        System.out.println(">>> GooglePlayAdapterImpl init start...");
        NetHttpTransport.Builder builder = new NetHttpTransport.Builder();
        if (proxyAddress != null && port != null) {
            builder.setProxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyAddress, port)));
        }
        packageName = googlePlayPackageName;
        NetHttpTransport transport = builder.build();
        try {
            GoogleCredential googleCredential = GoogleCredential.fromStream(Objects.requireNonNull(this.getClass()
                            .getClassLoader()
                            .getResourceAsStream(serviceAccountFileName)),
                    transport, GsonFactory.getDefaultInstance()).createScoped(SCOPES);
            AndroidPublisher publisher = new AndroidPublisher.Builder(transport,
                    GsonFactory.getDefaultInstance(), googleCredential).setApplicationName(packageName).build();
            purchases = publisher.purchases();
        } catch (Exception e) {
            log.error("Google Play Adapter init error,error message: {}", e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public ProductPurchase getProductPurchase(String purchaseToken, String platformProductId) {
        try {
            return purchases.products().get(packageName, platformProductId, purchaseToken).execute();
        } catch (IOException e) {
            log.error("Get product purchase error,error message: {}", e.getMessage(), e);
            return null;
        }
    }

    @Override
    public void doConsumeInAppProductPurchase(String purchaseToken, String platformProductId) {
        try {
            purchases.products().consume(packageName, platformProductId, purchaseToken).execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
