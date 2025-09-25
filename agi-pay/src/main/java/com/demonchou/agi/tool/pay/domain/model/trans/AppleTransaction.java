package com.demonchou.agi.tool.pay.domain.model.trans;

import com.apple.itunes.storekit.model.AutoRenewStatus;
import com.apple.itunes.storekit.model.Environment;
import com.apple.itunes.storekit.model.ExpirationIntent;
import com.apple.itunes.storekit.model.JWSRenewalInfoDecodedPayload;
import com.apple.itunes.storekit.model.JWSTransactionDecodedPayload;
import com.apple.itunes.storekit.model.LastTransactionsItem;
import com.apple.itunes.storekit.model.OfferDiscountType;
import com.apple.itunes.storekit.model.OfferType;
import com.apple.itunes.storekit.model.PriceIncreaseStatus;
import com.apple.itunes.storekit.model.Status;
import lombok.Data;
import lombok.experimental.Delegate;

/**
 * 苹果交易
 *
 * @author demonchou
 * @version AppleTransaction, 2025/8/26 11:47 demonchou
 */
@Data
public class AppleTransaction {
    private final LastTransactionsItem lastTransactionsItem;

    @Delegate
    private final JWSTransactionDecodedPayload jwsTransactionDecodedPayload;

    private final JWSRenewalInfoDecodedPayload jwsRenewalInfoDecodedPayload;

    public AppleTransaction(LastTransactionsItem lastTransactionsItem,
                            JWSTransactionDecodedPayload jwsTransactionDecodedPayload,
                            JWSRenewalInfoDecodedPayload jwsRenewalInfoDecodedPayload) {
        this.lastTransactionsItem = lastTransactionsItem;
        this.jwsTransactionDecodedPayload = jwsTransactionDecodedPayload;
        this.jwsRenewalInfoDecodedPayload = jwsRenewalInfoDecodedPayload;
    }

    public Status getStatus() {
        return lastTransactionsItem.getStatus();
    }

    public String getOriginalTransactionId() {
        return lastTransactionsItem.getOriginalTransactionId();
    }

    public String getSignedTransactionInfo() {
        return lastTransactionsItem.getSignedTransactionInfo();
    }

    public String getSignedRenewalInfo() {
        return lastTransactionsItem.getSignedRenewalInfo();
    }


    public ExpirationIntent getExpirationIntent() {
        return jwsRenewalInfoDecodedPayload.getExpirationIntent();
    }


    public String getAutoRenewProductId() {
        return jwsRenewalInfoDecodedPayload.getAutoRenewProductId();
    }

    public String getProductId() {
        return jwsRenewalInfoDecodedPayload.getProductId();
    }


    public AutoRenewStatus getAutoRenewStatus() {
        return jwsRenewalInfoDecodedPayload.getAutoRenewStatus();
    }

    public Boolean getIsInBillingRetryPeriod() {
        return jwsRenewalInfoDecodedPayload.getIsInBillingRetryPeriod();
    }


    public PriceIncreaseStatus getPriceIncreaseStatus() {
        return jwsRenewalInfoDecodedPayload.getPriceIncreaseStatus();
    }

    public Long getGracePeriodExpiresDate() {
        return jwsRenewalInfoDecodedPayload.getGracePeriodExpiresDate();
    }

    public OfferType getOfferType() {
        return jwsRenewalInfoDecodedPayload.getOfferType();
    }

    public String getOfferIdentifier() {
        return jwsRenewalInfoDecodedPayload.getOfferIdentifier();
    }

    public Long getSignedDate() {
        return jwsRenewalInfoDecodedPayload.getSignedDate();
    }

    public Environment getEnvironment() {
        return jwsRenewalInfoDecodedPayload.getEnvironment();
    }

    public Long getRecentSubscriptionStartDate() {
        return jwsRenewalInfoDecodedPayload.getRecentSubscriptionStartDate();
    }

    public Long getRenewalDate() {
        return jwsRenewalInfoDecodedPayload.getRenewalDate();
    }


    public Long getRenewalPrice() {
        return jwsRenewalInfoDecodedPayload.getRenewalPrice();
    }

    public String getCurrency() {
        return jwsRenewalInfoDecodedPayload.getCurrency();
    }

    public OfferDiscountType getOfferDiscountType() {
        return jwsRenewalInfoDecodedPayload.getOfferDiscountType();
    }
}
