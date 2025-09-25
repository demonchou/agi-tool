package com.demonchou.agi.tool.third.login.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 用户来源枚举
 *
 * @author demonchou
 * @version UserChannelEnum, 2025/8/11 16:19 demonchou
 */
@Getter
@AllArgsConstructor
public enum UserChannelEnum {
    /**
     * 用户来源 谷歌
     */
    GOOGLE("GOOGLE", "谷歌"),

    APPLE("APPLE", "苹果"),

    FACEBOOK("FACEBOOK", "脸书"),
    ;

    private final String code;
    private final String desc;

    public static UserChannelEnum getByCode(String code) {
        for (UserChannelEnum userChannelEnum : UserChannelEnum.values()) {
            if (userChannelEnum.getCode().equals(code)) {
                return userChannelEnum;
            }
        }
        return null;
    }
}
