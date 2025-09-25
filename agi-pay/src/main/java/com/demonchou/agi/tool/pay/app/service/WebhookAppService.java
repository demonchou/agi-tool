package com.demonchou.agi.tool.pay.app.service;

import com.demonchou.agi.tool.pay.domain.enums.PayChannelEnum;
import com.demonchou.agi.tool.pay.domain.processor.webhook.WebhookProcessor;
import com.demonchou.agi.tool.pay.domain.processor.webhook.WebhookProcessorFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;

/**
 * 回调处理服务
 *
 * @author demonchou
 * @version WebhookAppService, 2025/8/21 16:41 demonchou
 */
@Slf4j
@Service
public class WebhookAppService {

    @Autowired
    private WebhookProcessorFactory webhookProcessorFactory;

    public void webhook(HttpServletRequest request, PayChannelEnum payChannelEnum) {
        try {
            String notifyPayload = new String(request.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
            log.info("received webhook payChannelEnum:{}, notifyPayload:{}", payChannelEnum.getCode(), notifyPayload);
            WebhookProcessor purchaseProcessor = webhookProcessorFactory.getPurchaseProcessor(payChannelEnum);
            purchaseProcessor.process(notifyPayload);
        } catch (Exception e) {
            log.error("parse received webhook error, payChannelEnum:{}", payChannelEnum, e);
        }
    }
}
