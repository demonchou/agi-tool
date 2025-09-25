package com.demonchou.agi.tool.pay.domain.model.trans;

import lombok.Data;

/**
 * google支付一次性商品通知
 *
 * @author demonchou
 * @version GoogleOneTimeProductNotification, 2025/8/28 11:41 demonchou
 */
@Data
public class GoogleOneTimeProductNotification extends GoogleNotification {
    /**
     * 一次性商品的productId
     */
    private String sku;
}
