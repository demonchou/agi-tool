package com.demonchou.agi.tool.pay.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 购买类型
 *
 * @author demonchou
 * @version GoodsType, 2025/8/22 15:46 demonchou
 */
@Getter
@AllArgsConstructor
public enum ProductTypeEnum {
    SUBSCRIPTION("SUBSCRIPTION", "订阅"),
    INAPP("INAPP", "应用内购买/一次性付费"),
    ;

    private String code;
    private String desc;

    public static ProductTypeEnum getByCode(String code) {
        for (ProductTypeEnum productTypeEnum : ProductTypeEnum.values()) {
            if (productTypeEnum.getCode().equals(code)) {
                return productTypeEnum;
            }
        }
        return null;
    }

    public static boolean checkValidCode(String code) {
        return getByCode(code) != null;
    }

}
