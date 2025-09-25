package com.demonchou.agi.tool.pay.domain.processor.webhook;

import com.demonchou.agi.tool.pay.domain.enums.PayChannelEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 支付回调处理器工厂类
 *
 * @author demonchou
 * @version WebhookProcessorFactory, 2025/8/22 15:33 demonchou
 */
@Slf4j
@Service
public class WebhookProcessorFactory {

    private final Map<PayChannelEnum, WebhookProcessor> processorMap = new HashMap<>();

    @Autowired
    public WebhookProcessorFactory(List<WebhookProcessor> webhookProcessorList) {
        for (WebhookProcessor webhookProcessor : webhookProcessorList) {
            PayChannelEnum channel = webhookProcessor.getSupportedChannel();
            processorMap.put(channel, webhookProcessor);
        }
    }

    public WebhookProcessor getPurchaseProcessor(PayChannelEnum channel) {
        WebhookProcessor webhookProcessor = processorMap.get(channel);
        if (webhookProcessor == null) {
            log.error("Unsupported channel: {}", channel);
            return null;
        }
        return webhookProcessor;
    }
}
