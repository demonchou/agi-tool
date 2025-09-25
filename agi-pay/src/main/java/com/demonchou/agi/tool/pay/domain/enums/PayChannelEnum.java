package com.demonchou.agi.tool.pay.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 购买渠道枚举
 *
 * @author demonchou
 * @version PayChannelEnum, 2025/8/25 14:36 demonchou
 */
@Getter
@AllArgsConstructor
public enum PayChannelEnum {
    GOOGLE_PAY("GOOGLE_PAY"),
    APPLE_PAY("APPLE_PAY"),
    ;
    private String code;

    public static PayChannelEnum getByCode(String code) {
        for (PayChannelEnum value : PayChannelEnum.values()) {
            if (value.code.equals(code)) {
                return value;
            }
        }
        return null;
    }

    public static boolean checkValidCode(String code) {
        return getByCode(code) != null;
    }
}
