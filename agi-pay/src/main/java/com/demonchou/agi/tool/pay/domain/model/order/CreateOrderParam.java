package com.demonchou.agi.tool.pay.domain.model.order;

import com.demonchou.agi.tool.pay.app.dto.CreateOrderReqDto;
import com.demonchou.agi.tool.pay.domain.enums.PayChannelEnum;
import lombok.Data;

import java.util.Map;

/**
 * 创建订单参数值对象
 *
 * @author demonchou
 * @version CreateOrderParam, 2025/8/26 11:23 demonchou
 */
@Data
public class CreateOrderParam {
    /**
     * 支付渠道
     */
    private String payChannel;

    /**
     * 产品类型
     */
    private String productType;

    /**
     * 平台产品ID
     */
    private String platformProductId;

    /**
     * 支付请求ID
     */
    private String payRequestId;

    /**
     * 用户ID
     */
    private Long userId;
    /**
     * 商品ID
     */
    private Long goodsId;
    /**
     * 商品名称
     */
    private String goodsName;
    /**
     * 商品数量
     */
    private int quantity;
    /**
     * 价格
     */
    private Long price;

    /**
     * 支付 金额 单位：分
     */
    private Long payAmount;

    /**
     * 支付币种
     */
    private String currency;

    /**
     * 附加信息
     */
    private Map<String, Object> additionalInfo;

    public static CreateOrderParam createOrderParam(CreateOrderReqDto createOrderReqDto, String payRequestId) {

        Long payAmount = createOrderReqDto.getPayAmount();
        String payChannel = createOrderReqDto.getPayChannel();
        PayChannelEnum payChannelEnum = PayChannelEnum.getByCode(payChannel);
        if (payAmount != null) {
            if (payChannelEnum == PayChannelEnum.GOOGLE_PAY) {
                payAmount = payAmount / 10 / 1000; // 单位转换为分
            }
        }

        CreateOrderParam createOrderParam = new CreateOrderParam();
        createOrderParam.setPayChannel(createOrderReqDto.getPayChannel());
        createOrderParam.setUserId(createOrderReqDto.getUserId());
        createOrderParam.setGoodsId(createOrderReqDto.getGoodsId());
        createOrderParam.setGoodsName(createOrderReqDto.getGoodsName());
        createOrderParam.setPrice(createOrderReqDto.getPrice());
        createOrderParam.setPayAmount(payAmount);
        createOrderParam.setCurrency(createOrderReqDto.getCurrency());
        createOrderParam.setQuantity(null == createOrderReqDto.getQuantity() ? 1 : createOrderReqDto.getQuantity());
        createOrderParam.setPayRequestId(payRequestId);
        createOrderParam.setPlatformProductId(createOrderReqDto.getPlatformProductId());
        createOrderParam.setAdditionalInfo(createOrderReqDto.getAdditionalInfo());
        return createOrderParam;
    }
}
