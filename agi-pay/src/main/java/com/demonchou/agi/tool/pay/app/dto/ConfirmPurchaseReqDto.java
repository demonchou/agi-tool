package com.demonchou.agi.tool.pay.app.dto;

import lombok.Data;

/**
 * 确认购买请求参数
 *
 * @author demonchou
 * @version ConfirmPurchaseReqDto, 2025/8/26 10:18 demonchou
 */
@Data
public class ConfirmPurchaseReqDto extends BasePurchaseReqDto {

    /**
     * 平台商品ID
     */
    String platformProductId;

    /**
     * 购买凭证，谷歌为purchaseToken，苹果为transactionId
     */
    String purchaseToken;

    /**
     * 用户ID
     */
    Long userId;
}
