package com.demonchou.agi.tool.pay.app.dto;

import lombok.Data;

/**
 * 支付基类请求参数
 *
 * @author demonchou
 * @version BasePurchaseReqDto, 2025/8/26 16:33 demonchou
 */
@Data
public class BasePurchaseReqDto {

    /**
     * 支付渠道
     */
    private String payChannel;

    /**
     * 产品类型
     */
    private String productType;
}
