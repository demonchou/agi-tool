package com.demonchou.agi.tool.pay.app.dto;

import lombok.Data;

import java.util.Map;

/**
 * 创建订单请求参数
 *
 * @author demonchou
 * @version CreateOrderReqDto, 2025/8/21 18:17 demonchou
 */
@Data
public class CreateOrderReqDto extends BasePurchaseReqDto  {

    /**
     * 平台产品ID
     */
    private String platformProductId;

    /**
     * 用户ID
     */
    private Long userId;
    /**
     * 商品ID
     */
    private Long goodsId;
    /**
     * 商品名称
     */
    private String goodsName;
    /**
     * 商品数量
     */
    private Integer quantity;
    /**
     * 价格
     */
    private Long price;

    /**
     * 支付 金额 单位：分
     */
    private Long payAmount;

    /**
     * 支付币种
     */
    private String currency;

    /**
     * 附加信息
     */
    private Map<String, Object> additionalInfo;
}
