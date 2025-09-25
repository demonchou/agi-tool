package com.demonchou.agi.tool.third.login.dto;

import lombok.Data;

/**
 * 第三方账号信息
 *
 * @author demonchou
 * @version ThirdAccountDto, 2025/8/11 17:47 demonchou
 */
@Data
public class ThirdAccountDto {

    /**
     * 头像
     */
    private String avatar;

    /**
     * 昵称
     */
    private String nickName;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 第三方ID
     */
    private String thirdId;

    public ThirdAccountDto() {
    }

    public ThirdAccountDto(String avatar, String nickName, String email) {
        this.avatar = avatar;
        this.nickName = nickName;
        this.email = email;
    }

    public ThirdAccountDto(String avatar, String nickName, String email, String thirdId) {
        this.avatar = avatar;
        this.nickName = nickName;
        this.email = email;
        this.thirdId = thirdId;
    }

    public ThirdAccountDto(String email, String thirdId) {
        this.email = email;
        this.thirdId = thirdId;
    }
}
