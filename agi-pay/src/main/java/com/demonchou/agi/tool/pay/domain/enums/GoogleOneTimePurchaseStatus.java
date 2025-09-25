package com.demonchou.agi.tool.pay.domain.enums;

/**
 * Google One Time Purchase Status
 *
 * @author demonchou
 * @version GoogleOneTimePurchaseStatus, 2025/8/26 14:51 demonchou
 */
public interface GoogleOneTimePurchaseStatus {
    int PURCHASED = 0;

    int CANCELLED = 1;

    int PENDING = 2;
}
