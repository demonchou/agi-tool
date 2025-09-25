package com.demonchou.agi.tool.pay.domain.processor.webhook;

/**
 * 支付回调处理抽象类
 *
 * @author demonchou
 * @version AbstractWebhookProcessor, 2025/8/23 16:48 demonchou
 */
public abstract class AbstractWebhookProcessor implements WebhookProcessor {

    abstract boolean check(String signedPayload);

    abstract boolean processNotify(String notifyPayload);

    @Override
    public boolean process(String notifyPayload) {
        if (!check(notifyPayload)) {
            return false;
        }
        return processNotify(notifyPayload);
    }
}
