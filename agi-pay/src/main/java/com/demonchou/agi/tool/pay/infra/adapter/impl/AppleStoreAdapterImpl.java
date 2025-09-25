package com.demonchou.agi.tool.pay.infra.adapter.impl;

import com.apple.itunes.storekit.client.APIException;
import com.apple.itunes.storekit.model.Environment;
import com.apple.itunes.storekit.model.JWSTransactionDecodedPayload;
import com.apple.itunes.storekit.model.ResponseBodyV2DecodedPayload;
import com.apple.itunes.storekit.model.TransactionInfoResponse;
import com.apple.itunes.storekit.verification.VerificationException;
import com.demonchou.agi.tool.pay.domain.adapter.AppleStoreAdapter;
import com.demonchou.agi.tool.pay.infra.adapter.AppleStoreDelegator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;

/**
 * Apple store adapter
 *
 * @author demonchou
 * @version AppleStoreAdapterImpl, 2025/8/29 18:21 demonchou
 */
@Slf4j
@Service
public class AppleStoreAdapterImpl implements AppleStoreAdapter {

    private final AppleStoreDelegator client;


    public AppleStoreAdapterImpl(String keyId, String issuerId, String bundleId, String environment,
                                 Long appleId, String apiKeyPath, String rootCaPath) {
        System.out.println(">>> AppleStoreAdapterImpl init start >>>");

        try (InputStream p8In = Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream(apiKeyPath));
             InputStream cerIn = Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream(rootCaPath))) {

            String key = new String(p8In.readAllBytes());
            Set<byte[]> roots = Collections.singleton(cerIn.readAllBytes());

            Environment env = Environment.fromValue(environment);
            client = new AppleStoreDelegator(key, keyId, roots, issuerId, bundleId, env, appleId);
        } catch (Exception e) {
            log.error("fail to init apple store client,error: {}", e.getMessage(), e);
            throw new RuntimeException("fail to init apple store client,error: " + e.getMessage(), e);
        }
    }

    @Override
    public JWSTransactionDecodedPayload getApplePurchaseTransaction(String transactionId) {

        TransactionInfoResponse transactionInfo = null;
        try {
            transactionInfo = client.getTransactionInfo(transactionId);
            return client.verifyAndDecodeTransaction(transactionInfo.getSignedTransactionInfo());
        } catch (APIException | VerificationException | IOException e) {
            log.error("fail to get apple purchase transaction,error: {}", e.getMessage(), e);
            return null;
        }
    }

    @Override
    public ResponseBodyV2DecodedPayload getNotification(String signedPayload) {
        try {
            return client.verifyAndDecodeNotification(signedPayload);
        } catch (VerificationException e) {
            log.error("fail to verifyAndDecodeNotification from apple notification, error: {}", e.getMessage(), e);
            return null;
        }
    }

    @Override
    public JWSTransactionDecodedPayload verifyJWSTransaction(String signedPayload) {
        try {
            return client.verifyAndDecodeTransaction(signedPayload);
        } catch (VerificationException e) {
            log.error("fail to verifyAndDecodeTransaction from apple notification, error: {}", e.getMessage(), e);
            return null;
        }
    }
}
