package com.demonchou.agi.tool.third.login.processor;

import com.auth0.jwk.InvalidPublicKeyException;
import com.demonchou.agi.tool.third.login.dto.ThirdLoginRequestDto;
import com.demonchou.agi.tool.third.login.dto.ThirdAccountDto;
import com.demonchou.agi.tool.third.login.enums.UserChannelEnum;
import com.demonchou.agi.tool.third.login.utils.AppleLoginUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Apple登录处理器
 *
 * @author demonchou
 * @version AppleLoginProcessor, 2025/8/11 20:41 demonchou
 */
@Slf4j
@Service
public class AppleLoginProcessor extends AbstractLoginProcessor {

    @Override
    public UserChannelEnum getUserChannel() {
        return UserChannelEnum.APPLE;
    }

    @Override
    void checkParams(ThirdLoginRequestDto thirdLoginRequestDto) {
        if (StringUtils.isBlank(thirdLoginRequestDto.getCode()) && StringUtils.isBlank(thirdLoginRequestDto.getIdentityToken())) {
            throw new IllegalArgumentException("code or identityToken is required");
        }
    }


    public ThirdAccountDto login(ThirdLoginRequestDto requestDto) {
        return getAppleAccount(requestDto.getCode(), requestDto.getIdentityToken());
    }

    private ThirdAccountDto getAppleAccount(String code, String identityToken) {
        try {
            Map<String, String> account = requestAppleAccount(code, identityToken);
            ThirdAccountDto result = new ThirdAccountDto();
            result.setEmail(account.get("email"));
            result.setThirdId(account.get("appleId"));
            return result;
        } catch (Exception e) {
            log.error("获取apple信息出现异常", e);
        }
        return null;
    }

    private Map<String, String> requestAppleAccount(String code, String identityToken) throws InvalidPublicKeyException {
        return AppleLoginUtil.checkIdentityToken(identityToken);
    }
}
