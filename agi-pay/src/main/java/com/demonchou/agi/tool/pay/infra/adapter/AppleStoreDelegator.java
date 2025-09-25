package com.demonchou.agi.tool.pay.infra.adapter;

import com.apple.itunes.storekit.client.APIException;
import com.apple.itunes.storekit.client.AppStoreServerAPIClient;
import com.apple.itunes.storekit.model.Environment;
import com.apple.itunes.storekit.model.JWSRenewalInfoDecodedPayload;
import com.apple.itunes.storekit.model.JWSTransactionDecodedPayload;
import com.apple.itunes.storekit.model.ResponseBodyV2DecodedPayload;
import com.apple.itunes.storekit.model.Status;
import com.apple.itunes.storekit.model.StatusResponse;
import com.apple.itunes.storekit.model.TransactionInfoResponse;
import com.apple.itunes.storekit.verification.SignedDataVerifier;
import com.apple.itunes.storekit.verification.VerificationException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

/**
 * apple store delegator
 *
 * @author demonchou
 * @version AppleStoreDelegator, 2025/8/29 18:27 demonchou
 */
public class AppleStoreDelegator {

    private final AppStoreServerAPIClient client1;

    private final AppStoreServerAPIClient client2;

    private final SignedDataVerifier verifier1;

    private final SignedDataVerifier verifier2;

    public AppleStoreDelegator(String key, String keyId, Set<byte[]> roots2, String issuerId, String bundleId, Environment environment, Long appleId) {

        client1 = new AppStoreServerAPIClient(key, keyId, issuerId, bundleId, environment);
        verifier1 = new SignedDataVerifier(toInputStreams(roots2), bundleId, appleId, environment, true);

        if (environment == Environment.PRODUCTION) {
            client2 = new AppStoreServerAPIClient(key, keyId, issuerId, bundleId, Environment.SANDBOX);
            verifier2 = new SignedDataVerifier(toInputStreams(roots2), bundleId, appleId, Environment.SANDBOX, true);
        } else {
            client2 = null;
            verifier2 = null;
        }
    }

    public StatusResponse getAllSubscriptionStatuses(String transactionId, Status[] status) throws APIException, IOException {
        try {
            return client1.getAllSubscriptionStatuses(transactionId, status);
        } catch (APIException e) {
            if (e.getHttpStatusCode() == 401 || e.getHttpStatusCode() == 404 && client2 != null) {
                return client2.getAllSubscriptionStatuses(transactionId, status);
            }
            throw e;
        }
    }

    public TransactionInfoResponse getTransactionInfo(String transactionId) throws APIException, IOException {
        try {
            return client1.getTransactionInfo(transactionId);
        } catch (APIException e) {
            if (e.getHttpStatusCode() == 401 || e.getHttpStatusCode() == 404 && client2 != null) {
                return client2.getTransactionInfo(transactionId);
            }
            throw e;
        }
    }

    public JWSTransactionDecodedPayload verifyAndDecodeTransaction(String signedTransaction) throws VerificationException {
        try {
            return verifier1.verifyAndDecodeTransaction(signedTransaction);
        } catch (VerificationException e) {
            if (verifier2 != null) {
                return verifier2.verifyAndDecodeTransaction(signedTransaction);
            }
            throw e;
        }
    }

    public ResponseBodyV2DecodedPayload verifyAndDecodeNotification(String signedPayload) throws VerificationException {
        try {
            return verifier1.verifyAndDecodeNotification(signedPayload);
        } catch (VerificationException e) {
            if (verifier2 != null) {
                return verifier2.verifyAndDecodeNotification(signedPayload);
            }
            throw e;
        }
    }

    public JWSRenewalInfoDecodedPayload verifyAndDecodeRenewalInfo(String signedRenewalInfo) throws VerificationException {
        try {
            return verifier1.verifyAndDecodeRenewalInfo(signedRenewalInfo);
        } catch (VerificationException e) {
            if (verifier2 != null) {
                return verifier2.verifyAndDecodeRenewalInfo(signedRenewalInfo);
            }
            throw e;
        }
    }

    private Set<InputStream> toInputStreams(Set<byte[]> roots) {
        Set<InputStream> set = new HashSet<>();
        for (byte[] root : roots) {
            set.add(new ByteArrayInputStream(root));
        }
        return set;
    }
}
