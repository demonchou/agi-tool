package com.demonchou.agi.tool.pay.app.dto;

import lombok.Data;

/**
 * @author demonchou
 * @version OrderDto, 2025/8/21 18:16 demonchou
 */
@Data
public class CreateOrderResDto {

    /**
     * 商品Id
     */
    private Long goodsId;

    /**
     * 平台商品Id
     */
    private String platformProductId;

    /**
     * 商品类型
     */
    private String productType;

    /**
     * 本地购买标识
     */
    private String profileId;


    /**
     * 订单ID
     */
    private Long orderId;
}
