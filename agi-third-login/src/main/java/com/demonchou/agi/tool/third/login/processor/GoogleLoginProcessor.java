package com.demonchou.agi.tool.third.login.processor;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.demonchou.agi.tool.common.utils.EnvironmentUtil;
import com.demonchou.agi.tool.common.utils.HttpProxyUtil;
import com.demonchou.agi.tool.common.utils.HttpUtil;
import com.demonchou.agi.tool.third.login.dto.ThirdLoginRequestDto;
import com.demonchou.agi.tool.third.login.dto.ThirdAccountDto;
import com.demonchou.agi.tool.third.login.enums.UserChannelEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

import javax.annotation.PostConstruct;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Google登录处理器
 *
 * @author demonchou
 * @version GoogleLoginProcessor, 2025/8/11 20:43 demonchou
 */
@Slf4j
public class GoogleLoginProcessor extends AbstractLoginProcessor {
    private final static String GOOGLE_USER_INFO_URL = "https://www.googleapis.com/oauth2/v1/userinfo";

    private final String androidGoogleClientId;
    private final String proxyAddress;
    private final Integer port;

    public GoogleLoginProcessor(String androidGoogleClientId, String proxyAddress, Integer port) {
        this.androidGoogleClientId = androidGoogleClientId;
        this.proxyAddress = proxyAddress;
        this.port = port;
    }

    private GoogleIdTokenVerifier androidVerifier;

    @Override
    public UserChannelEnum getUserChannel() {
        return UserChannelEnum.GOOGLE;
    }

    @Override
    void checkParams(ThirdLoginRequestDto thirdLoginRequestDto) {
        Assert.notNull(thirdLoginRequestDto, "第三方账号不能为空");
        Assert.hasText(thirdLoginRequestDto.getAccessToken(), "accessToken不能为空");
    }

    @Override
    public ThirdAccountDto login(ThirdLoginRequestDto thirdLoginRequestDto) {
        return getGoogleAccount(thirdLoginRequestDto.getAccessToken(), thirdLoginRequestDto.getPlatform());
    }

    private ThirdAccountDto getGoogleAccount(String credential, String platform) {
        if (Objects.equals(platform, "WEB")) {
            return getGoogleAccountInfo(credential);
        }
        GoogleIdToken googleIdToken = getGoogleIdToken(credential);
        GoogleIdToken.Payload payload = googleIdToken.getPayload();
        String email = payload.getEmail();
        String avatar = (String) payload.get("picture");
        String nickName = (String) payload.get("name");

        return new ThirdAccountDto(avatar, nickName, email);
    }

    private GoogleIdToken getGoogleIdToken(String credential) {
        try {
            GoogleIdToken verify = androidVerifier.verify(credential);
            Assert.notNull(verify, "invalid token");
            return verify;
        } catch (Exception e) {
            log.error("Google token verify failed, errMsg:{}", e.getMessage(), e);
            System.out.println("Google token verify failed, errMsg: " + e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    private ThirdAccountDto getGoogleAccountInfo(String accessToken) {
        Map<String, String> header = new HashMap<>();
        header.put("Authorization", "Bearer " + accessToken);
        String accountInfo;

        if (EnvironmentUtil.isOnline()) {
            accountInfo = HttpUtil.getWithHeader(GOOGLE_USER_INFO_URL, header);
        } else {
            accountInfo = HttpProxyUtil.getWithHeader(GOOGLE_USER_INFO_URL, header);
        }

        if (StringUtils.isEmpty(accountInfo)) {
            return null;
        }

        JSONObject accountJson = JSON.parseObject(accountInfo);
        String email = accountJson.getString("email");
        if (StringUtils.isEmpty(email)) {
            return null;
        }

        String avatar = accountJson.getString("picture");
        String nickName = accountJson.getString("name");
        return new ThirdAccountDto(avatar, nickName, email);
    }

    @PostConstruct
    public void init() {
        NetHttpTransport.Builder builder = new NetHttpTransport.Builder();
        if (StringUtils.isNotBlank(proxyAddress) && port != null) {
            builder.setProxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyAddress, port)));
        }

        androidVerifier = new GoogleIdTokenVerifier.Builder(builder.build(), new GsonFactory())
                .setAudience(Collections.singleton(androidGoogleClientId))
                .build();
        log.info("GoogleLoginProcessor init success");
        System.out.println("GoogleLoginProcessor init success");
    }
}
