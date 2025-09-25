package com.demonchou.agi.tool.pay.infra.persistence.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * 订单entity
 *
 * @author demonchou
 * @version OrderPO, 2025/8/21 14:53 demonchou
 */
@Data
@TableName("`order`")
public class OrderPO {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户ID，关联用户表
     */
    @TableField("user_id")
    private Long userId;

    /**
     * 商品ID，关联商品表
     */
    @TableField("goods_id")
    private Long goodsId;

    /**
     * 商品名称
     */
    @TableField("goods_name")
    private String goodsName;

    /**
     * 支付渠道
     */
    @TableField("pay_channel")
    private String payChannel;

    /**
     * 支付请求ID，用于幂等控制
     */
    @TableField("pay_request_id")
    private String payRequestId;

    /**
     * 商品在外部平台的ID
     */
    @TableField("platform_product_id")
    private String platformProductId;

    /**
     * 外部平台返回的订单号
     */
    @TableField("platform_order_id")
    private String platformOrderId;

    /**
     * 交易时间，单位：秒
     */
    @TableField("pay_date")
    private Long payDate;

    /**
     * 商品标价（单位：分），以本币种为准
     */
    @TableField("price")
    private Long price;

    /**
     * 实际支付金额（单位：分）
     */
    @TableField("pay_amount")
    private Long payAmount;

    /**
     * 分与主币单位的转换比例，例如 CNY 为 100，JPY 为 1
     */
    @TableField("conversion_scale")
    private Integer conversionScale;

    /**
     * 币种代码，例如 USD、CNY
     */
    @TableField("currency")
    private String currency;

    /**
     * 订单状态，如 CREATED, PAID, REFUNDED 等
     */
    @TableField("status")
    private String status;

    /**
     * 退款单ID，关联退款记录
     */
    @TableField("refund_id")
    private String refundId;

    @TableField("create_at")
    private Date createAt;

    @TableField("update_at")
    private Date updateAt;
}
