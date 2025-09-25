package com.demonchou.agi.tool.third.login.dto;

import lombok.Data;

/**
 * 登录请求DTO
 *
 * @author demonchou
 * @version LoginRequestDTO, 2025/8/11 16:55 demonchou
 */
@Data
public class ThirdLoginRequestDto {

    /**
     * 登录渠道
     */
    private String userChannel;

    /**
     * Apple授权 identityToken
     */
    private String identityToken;

    /**
     * Apple授权 code
     */
    private String code;

    /**
     * Google授权 accessToken
     */
    private String accessToken;

    /**
     * 设备ID
     */
    private String deviceId;

    /**
     * 平台 android/web
     */
    private String platform;
}
