package com.demonchou.agi.tool.pay.domain.model.order;

import lombok.Data;

/**
 * 创建订单后的结果返回
 *
 * @author demonchou
 * @version CreateOrderResponse, 2025/8/26 11:23 demonchou
 */
@Data
public class CreateOrderResponse {
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


    private Long orderId;
}
