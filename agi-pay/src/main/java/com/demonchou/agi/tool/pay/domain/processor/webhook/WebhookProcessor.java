package com.demonchou.agi.tool.pay.domain.processor.webhook;

import com.demonchou.agi.tool.pay.domain.enums.PayChannelEnum;

/**
 * 支付回调处理接口
 *
 * @author demonchou
 * @version WebhookProcessor, 2025/8/23 16:47 demonchou
 */
public interface WebhookProcessor {

    PayChannelEnum getSupportedChannel();

    boolean process(String notifyPayload);
}
