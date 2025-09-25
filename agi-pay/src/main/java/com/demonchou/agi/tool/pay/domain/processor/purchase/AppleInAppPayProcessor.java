package com.demonchou.agi.tool.pay.domain.processor.purchase;

import com.demonchou.agi.tool.pay.app.dto.ConfirmPurchaseReqDto;
import com.demonchou.agi.tool.pay.app.dto.CreateOrderReqDto;
import com.demonchou.agi.tool.pay.domain.enums.ProductTypeEnum;
import com.demonchou.agi.tool.pay.domain.enums.PayChannelEnum;
import com.demonchou.agi.tool.pay.domain.model.order.CreateOrderParam;
import com.demonchou.agi.tool.pay.domain.model.order.CreateOrderResponse;
import com.demonchou.agi.tool.pay.domain.model.order.Order;
import com.demonchou.agi.tool.pay.domain.repository.OrderRepository;
import com.demonchou.agi.tool.pay.domain.service.ApplePayService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.UUID;

/**
 * 苹果内购支付处理器
 *
 * @author demonchou
 * @version AppleInAppPayProcessor, 2025/8/21 18:03 demonchou
 */
@Slf4j
@Service
public class AppleInAppPayProcessor extends AbstractPayProcessor {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ApplePayService applePayService;

    @Override
    public PayChannelEnum getSupportedChannel() {
        return PayChannelEnum.APPLE_PAY;
    }

    @Override
    public ProductTypeEnum getSupportedGoodsType() {
        return ProductTypeEnum.INAPP;
    }

    @Override
    boolean checkCreateOrder(CreateOrderReqDto createOrderReqDto) {
        Assert.notNull(createOrderReqDto, "createOrderReqDto is null");
        Assert.notNull(createOrderReqDto.getPayChannel(), "payChannel is null");
        return true;
    }

    @Override
    CreateOrderResponse createOrder(CreateOrderReqDto createOrderReqDto) {
        String appAccountToken = UUID.randomUUID().toString();
        CreateOrderParam createOrderParam = CreateOrderParam.createOrderParam(createOrderReqDto, appAccountToken);
        Order order = Order.createOrder(createOrderParam);
        Long orderId = orderRepository.save(order);

        CreateOrderResponse response = new CreateOrderResponse();
        response.setOrderId(orderId);
        response.setProfileId(appAccountToken);
        response.setProductType("APPLE_INAPP");
        response.setPlatformProductId(createOrderReqDto.getPlatformProductId());
        return response;
    }

    @Override
    void afterCreateOrder(CreateOrderReqDto createOrderReqDto, Long orderId) {
        // do nothing
    }

    @Override
    boolean checkConfirmPurchase(ConfirmPurchaseReqDto confirmPurchaseReqDto) {
        Assert.notNull(confirmPurchaseReqDto, "confirmPurchaseReqDto is null");
        Assert.hasText(confirmPurchaseReqDto.getPurchaseToken(), "purchaseToken is null");
        return true;
    }

    @Override
    boolean confirmPurchase(ConfirmPurchaseReqDto confirmPurchaseReqDto) {
        // 对应苹果商店的 transactionId
        String transactionId = confirmPurchaseReqDto.getPurchaseToken();
        return applePayService.confirmPurchase(transactionId);
    }
}
