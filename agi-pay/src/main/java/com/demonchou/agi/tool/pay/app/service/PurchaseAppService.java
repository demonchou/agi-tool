package com.demonchou.agi.tool.pay.app.service;

import com.demonchou.agi.tool.pay.app.dto.ConfirmPurchaseReqDto;
import com.demonchou.agi.tool.pay.app.dto.CreateOrderReqDto;
import com.demonchou.agi.tool.pay.app.dto.CreateOrderResDto;
import com.demonchou.agi.tool.pay.domain.enums.PayChannelEnum;
import com.demonchou.agi.tool.pay.domain.enums.ProductTypeEnum;
import com.demonchou.agi.tool.pay.domain.processor.purchase.PayProcessor;
import com.demonchou.agi.tool.pay.domain.processor.purchase.PurchaseProcessorFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 * 购买服务
 *
 * @author demonchou
 * @version PurchaseAppService, 2025/8/21 16:40 demonchou
 */
@Service
public class PurchaseAppService {

    @Autowired
    private PurchaseProcessorFactory purchaseProcessorFactory;

    public PurchaseAppService() {
    }

    public CreateOrderResDto purchase(CreateOrderReqDto createOrderReqDto) {
        // 参数校验
        commonParamsCheck(createOrderReqDto);

        // 获取处理器并执行
        PayProcessor processor =
                purchaseProcessorFactory.getProcessor(PayChannelEnum.getByCode(createOrderReqDto.getPayChannel()),
                        ProductTypeEnum.getByCode(createOrderReqDto.getProductType()));
        return processor.purchase(createOrderReqDto);
    }

    public boolean confirm(ConfirmPurchaseReqDto confirmPurchaseReqDto) {

        // 获取处理器并执行
        PayProcessor processor =
                purchaseProcessorFactory.getProcessor(PayChannelEnum.getByCode(confirmPurchaseReqDto.getPayChannel()),
                        ProductTypeEnum.getByCode(confirmPurchaseReqDto.getProductType()));
        return processor.confirm(confirmPurchaseReqDto);
    }

    private static void commonParamsCheck(CreateOrderReqDto createOrderReqDto) {
        Assert.notNull(createOrderReqDto, "createOrderReqDto cannot be null");
        Assert.hasText(createOrderReqDto.getPayChannel(), "payChannel cannot be empty");
        Assert.hasText(createOrderReqDto.getProductType(), "productType cannot be empty");
        Assert.isTrue(PayChannelEnum.checkValidCode(createOrderReqDto.getPayChannel()), "payChannel is not supported");
        Assert.isTrue(ProductTypeEnum.checkValidCode(createOrderReqDto.getProductType()), "goodsType cannot be null");
    }
}
