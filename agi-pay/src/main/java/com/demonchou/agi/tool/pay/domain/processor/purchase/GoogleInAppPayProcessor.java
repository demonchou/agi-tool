package com.demonchou.agi.tool.pay.domain.processor.purchase;

import com.demonchou.agi.tool.pay.app.dto.ConfirmPurchaseReqDto;
import com.demonchou.agi.tool.pay.app.dto.CreateOrderReqDto;
import com.demonchou.agi.tool.pay.domain.enums.ProductTypeEnum;
import com.demonchou.agi.tool.pay.domain.enums.PayChannelEnum;
import com.demonchou.agi.tool.pay.domain.model.order.CreateOrderParam;
import com.demonchou.agi.tool.pay.domain.model.order.CreateOrderResponse;
import com.demonchou.agi.tool.pay.domain.model.order.Order;
import com.demonchou.agi.tool.pay.domain.repository.OrderRepository;
import com.demonchou.agi.tool.pay.domain.service.GooglePayService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.UUID;

/**
 * 谷歌内购支付处理器
 *
 * @author demonchou
 * @version GoogleInAppPayProcessor, 2025/8/21 18:04 demonchou
 */
@Slf4j
@Service
public class GoogleInAppPayProcessor extends AbstractPayProcessor {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private GooglePayService googlePayService;

    @Override
    public PayChannelEnum getSupportedChannel() {
        return PayChannelEnum.GOOGLE_PAY;
    }

    @Override
    public ProductTypeEnum getSupportedGoodsType() {
        return ProductTypeEnum.INAPP;
    }

    @Override
    boolean checkCreateOrder(CreateOrderReqDto createOrderReqDto) {
        return true;
    }

    @Override
    CreateOrderResponse createOrder(CreateOrderReqDto createOrderReqDto) {
        String payRequestId = UUID.randomUUID().toString();
        CreateOrderParam createOrderParam = CreateOrderParam.createOrderParam(createOrderReqDto, payRequestId);
        Order order = Order.createOrder(createOrderParam);
        Long orderId = orderRepository.save(order);
        CreateOrderResponse response = new CreateOrderResponse();
        response.setOrderId(orderId);
        response.setProfileId(payRequestId);
        response.setProductType(ProductTypeEnum.INAPP.getCode());
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
        Assert.hasText(confirmPurchaseReqDto.getPlatformProductId(), "platformProductId is empty");
        Assert.hasText(confirmPurchaseReqDto.getPurchaseToken(), "purchaseToken is empty");
        return true;
    }

    @Override
    boolean confirmPurchase(ConfirmPurchaseReqDto confirmPurchaseReqDto) {
        String platformProductId = confirmPurchaseReqDto.getPlatformProductId();
        String purchaseToken = confirmPurchaseReqDto.getPurchaseToken();
        return googlePayService.confirmPurchase(platformProductId, purchaseToken);
    }
}
