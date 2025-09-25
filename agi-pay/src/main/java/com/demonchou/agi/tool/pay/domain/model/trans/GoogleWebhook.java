package com.demonchou.agi.tool.pay.domain.model.trans;

import lombok.Data;

/**
 * Google 支付回调 Webhook 实体类
 * @author demonchou
 * @version GoogleWebhook, 2025/8/28 11:31 demonchou
 */
@Data
public class GoogleWebhook {
    private GoogleWebhookMessage message;

    private String subscription;
}
