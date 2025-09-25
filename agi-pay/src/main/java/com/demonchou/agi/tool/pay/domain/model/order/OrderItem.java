package com.demonchou.agi.tool.pay.domain.model.order;

import lombok.Data;

/**
 * 订单项
 *
 * @author demonchou
 * @version OrderItem, 2025/8/21 18:07 demonchou
 */
@Data
public class OrderItem {

    /**
     * 商品ID
     */
    private Long goodsId;

    /**
     * 商品平台ID
     */
    private String platformProductId;

    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * 支付渠道
     */
    private String payChannel;

    /**
     * 支付请求ID
     */
    private String payRequestId;

    /**
     * 外部平台订单号
     */
    private String platformOrderId;

    /**
     * 外部平台订单日期
     */
    private Long payDate;

    /**
     * 商品价格
     */
    private Long price;

    /**
     * 支付金额
     */
    private Long payAmount;

    /**
     * 分转换比例
     */
    private Integer conversionScale;

    /**
     * 币种，USD/CNY
     */
    private String currency;

    /**
     * 退款ID
     */
    private String refundId;

    /**
     * 附加信息
     */
    private String additionalInfo;
}
