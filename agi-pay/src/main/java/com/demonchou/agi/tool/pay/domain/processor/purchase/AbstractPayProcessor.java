package com.demonchou.agi.tool.pay.domain.processor.purchase;

import com.demonchou.agi.tool.pay.app.dto.ConfirmPurchaseReqDto;
import com.demonchou.agi.tool.pay.app.dto.CreateOrderReqDto;
import com.demonchou.agi.tool.pay.app.dto.CreateOrderResDto;
import com.demonchou.agi.tool.pay.domain.model.order.CreateOrderResponse;

/**
 * 支付处理器抽象类
 *
 * @author demonchou
 * @version AbstractPayProcessor, 2025/8/25 14:17 demonchou
 */
public abstract class AbstractPayProcessor implements PayProcessor {
    @Override
    public CreateOrderResDto purchase(CreateOrderReqDto createOrderReqDto) {
        // 参数校验
        if (!checkCreateOrder(createOrderReqDto)) {
            return null;
        }

        // 创建订单
        CreateOrderResponse createOrderResponse = createOrder(createOrderReqDto);

        // 订单处理后续操作
        afterCreateOrder(createOrderReqDto, createOrderResponse.getOrderId());

        // 返回订单信息
        CreateOrderResDto createOrderResDto = new CreateOrderResDto();
        createOrderResDto.setOrderId(createOrderResponse.getOrderId());
        createOrderResDto.setPlatformProductId(createOrderResponse.getPlatformProductId());
        createOrderResDto.setProfileId(createOrderResponse.getProfileId());
        createOrderResDto.setProductType(createOrderResponse.getProductType());
        createOrderResDto.setGoodsId(createOrderResDto.getGoodsId());
        return createOrderResDto;
    }

    // 参数校验
    abstract boolean checkCreateOrder(CreateOrderReqDto createOrderReqDto);

    abstract CreateOrderResponse createOrder(CreateOrderReqDto createOrderReqDto);

    abstract void afterCreateOrder(CreateOrderReqDto createOrderReqDto, Long orderId);

    @Override
    public boolean confirm(ConfirmPurchaseReqDto confirmPurchaseReqDto) {
        // 参数校验
        if (!checkConfirmPurchase(confirmPurchaseReqDto)) {
            return false;
        }

        // 订单处理后续操作
        return confirmPurchase(confirmPurchaseReqDto);
    }

    abstract boolean checkConfirmPurchase(ConfirmPurchaseReqDto confirmPurchaseReqDto);

    abstract boolean confirmPurchase(ConfirmPurchaseReqDto confirmPurchaseReqDto);
}
