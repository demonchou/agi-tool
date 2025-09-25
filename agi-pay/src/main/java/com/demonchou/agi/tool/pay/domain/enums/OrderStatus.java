package com.demonchou.agi.tool.pay.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 订单状态枚举
 *
 * @author demonchou
 * @version OrderStatus, 2025/8/26 10:47 demonchou
 */
@Getter
@AllArgsConstructor
public enum OrderStatus {
    INIT("INIT", "初始化"),
    PAYING("PAYING", "支付中"),
    PAID("PAID", "已支付"),
    CLOSED("CLOSED", "已关闭"),
    FAILED("FAILED", "支付失败"),
    REFUNDED("REFUNDED", "已退款"),
    ;
    private String code;
    private String desc;
}
