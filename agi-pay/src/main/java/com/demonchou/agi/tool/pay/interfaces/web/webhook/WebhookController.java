package com.demonchou.agi.tool.pay.interfaces.web.webhook;

import com.demonchou.agi.tool.pay.app.service.WebhookAppService;
import com.demonchou.agi.tool.pay.domain.enums.PayChannelEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 支付回调处理
 *
 * @author demonchou
 * @version WebhookController, 2025/8/23 16:49 demonchou
 */
@RestController
@RequestMapping("pay/webhook")
public class WebhookController {

    @Autowired
    private WebhookAppService webhookService;

    @PostMapping("apple")
    public void appleWebhook(HttpServletRequest request) {
        webhookService.webhook(request, PayChannelEnum.APPLE_PAY);
    }

    @PostMapping("google")
    public void googleWebhook(HttpServletRequest request) {
        webhookService.webhook(request, PayChannelEnum.GOOGLE_PAY);
    }
}
