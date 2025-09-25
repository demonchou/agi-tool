package com.demonchou.agi.tool.pay.domain.processor.purchase;

import com.demonchou.agi.tool.pay.app.dto.ConfirmPurchaseReqDto;
import com.demonchou.agi.tool.pay.app.dto.CreateOrderReqDto;
import com.demonchou.agi.tool.pay.app.dto.CreateOrderResDto;
import com.demonchou.agi.tool.pay.domain.enums.ProductTypeEnum;
import com.demonchou.agi.tool.pay.domain.enums.PayChannelEnum;

/**
 * 支付处理器接口
 *
 * @author demonchou
 * @version PurchaseProcessor, 2025/8/25 14:17 demonchou
 */
public interface PayProcessor {

    /**
     * 获取该策略支持的支付渠道
     *
     * @return 支付渠道
     */
    PayChannelEnum getSupportedChannel();

    /**
     * 获取该策略支持的商品类型
     *
     * @return 商品类型
     */
    ProductTypeEnum getSupportedGoodsType();

    /**
     * 参数校验
     */
    CreateOrderResDto purchase(CreateOrderReqDto createOrderReqDto);

    /**
     * 确认订单
     * 校验订单信息，
     * 更新平台订单号，订单状态(支付成功，但是待发货)
     */
    boolean confirm(ConfirmPurchaseReqDto confirmPurchaseReqDto);
}
