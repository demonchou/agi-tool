package com.demonchou.agi.tool.pay.domain.model.trans;

import lombok.Data;

/**
 * google支付webhook消息体
 *
 * @author demonchou
 * @version GoogleWebhookMessage, 2025/8/28 11:31 demonchou
 */
@Data
public class GoogleWebhookMessage {
    /**
     * base64编码字符串
     */
    private String data;

    private String messageId;

    private GoogleDeveloperNotification resolvedData;
}
