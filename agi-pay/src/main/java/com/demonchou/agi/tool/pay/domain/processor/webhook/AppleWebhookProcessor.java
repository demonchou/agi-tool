package com.demonchou.agi.tool.pay.domain.processor.webhook;

import com.demonchou.agi.tool.pay.domain.enums.PayChannelEnum;
import com.demonchou.agi.tool.pay.domain.service.ApplePayService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Apple Pay 支付回调处理器
 *
 * @author demonchou
 * @version AppleWebhookProcessor, 2025/8/21 18:04 demonchou
 */
@Slf4j
@Service
public class AppleWebhookProcessor extends AbstractWebhookProcessor {
    @Autowired
    private ApplePayService applePayService;

    @Override
    public PayChannelEnum getSupportedChannel() {
        return PayChannelEnum.APPLE_PAY;
    }

    @Override
    boolean check(String payload) {
        return false;
    }

    @Override
    boolean processNotify(String payload) {
        return applePayService.processNotification(payload);
    }
}
