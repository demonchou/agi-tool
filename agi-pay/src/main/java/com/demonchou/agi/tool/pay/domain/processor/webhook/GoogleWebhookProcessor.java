package com.demonchou.agi.tool.pay.domain.processor.webhook;

import com.alibaba.fastjson2.JSON;
import com.demonchou.agi.tool.pay.domain.enums.PayChannelEnum;
import com.demonchou.agi.tool.pay.domain.model.trans.GoogleDeveloperNotification;
import com.demonchou.agi.tool.pay.domain.model.trans.GoogleWebhook;
import com.demonchou.agi.tool.pay.domain.service.GooglePayService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Google支付回调处理器
 *
 * @author demonchou
 * @version GoogleWebhookProcessor, 2025/8/21 18:05 demonchou
 */
@Slf4j
@Service
public class GoogleWebhookProcessor extends AbstractWebhookProcessor {
    @Autowired
    private GooglePayService googlePlayService;

    @Override
    public PayChannelEnum getSupportedChannel() {
        return PayChannelEnum.GOOGLE_PAY;
    }

    @Override
    boolean check(String notifyPayload) {
        return false;
    }

    @Override
    boolean processNotify(String notifyPayload) {
        GoogleWebhook googleWebhook;
        GoogleDeveloperNotification notification;
        try {
            log.info("google webhook body:{}", notifyPayload);
            googleWebhook = JSON.parseObject(notifyPayload, GoogleWebhook.class);
            if (googleWebhook == null || googleWebhook.getMessage() == null
                    || (notification = googleWebhook.getMessage().getResolvedData()) == null) {
                log.error("google webhook parse failed, notifyPayload:{}", notifyPayload);
                return false;
            }
        } catch (Exception e) {
            log.error("google webhook parse error:{}", e.getMessage(), e);
            return false;
        }

//        if (!Objects.equals(notification.getPackageName(), googl.getPackageName())) {
//            log.error("收到不属于 {} 安卓版的回调信息,packageName:{}", googlePlayService.getPackageName(), notification.getPackageName());
//            return;
//        }

        if (null != notification.getOneTimeProductNotification()) {
            googlePlayService.processOneTimeProductNotification(notification);
        }

        return true;
    }
}
