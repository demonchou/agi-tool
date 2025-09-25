package com.demonchou.agi.tool.third.login.processor;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.auth.UserRecord;
import com.demonchou.agi.tool.third.login.dto.ThirdAccountDto;
import com.demonchou.agi.tool.third.login.dto.ThirdLoginRequestDto;
import com.demonchou.agi.tool.third.login.enums.UserChannelEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

import java.io.IOException;
import java.util.Objects;

/**
 * facebook 登录处理器
 *
 * @author demonchou
 * @version FacebookLoginProcessor, 2025/8/22 14:42 demonchou
 */
@Slf4j
public class FacebookLoginProcessor extends AbstractLoginProcessor {

    private final String appId;
    private final String appSecret;

    // Graph API 基础 URL
    private static final String GRAPH_API_BASE = "https://graph.facebook.com/v23.0"; // 使用最新稳定版本

    public FacebookLoginProcessor(String appId, String appSecret, String firebaseServiceAccountFilePath) {
        this.appId = appId;
        this.appSecret = appSecret;
        if (FirebaseApp.getApps().isEmpty()) {
            try {
                FirebaseApp.initializeApp(FirebaseOptions.builder()
                        .setCredentials(GoogleCredentials.fromStream(Objects.requireNonNull(getClass().getResourceAsStream(firebaseServiceAccountFilePath))))
                        .build());
            } catch (IOException e) {
                log.error("初始化 FirebaseApp 失败", e);
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public UserChannelEnum getUserChannel() {
        return UserChannelEnum.FACEBOOK;
    }


    @Override
    void checkParams(ThirdLoginRequestDto thirdLoginRequestDto) {
        Assert.notNull(thirdLoginRequestDto, "thirdLoginRequestDto is null");
        Assert.hasText(thirdLoginRequestDto.getAccessToken(), "accessToken is null or empty");
    }

    @Override
    ThirdAccountDto login(ThirdLoginRequestDto thirdLoginRequestDto) {
        return verifyAndGetAccountInfo(thirdLoginRequestDto.getAccessToken());
    }

    private ThirdAccountDto verifyAndGetAccountInfo(String idToken) {
        try {
            // 验证 Firebase ID token
            FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(idToken);
            Assert.notNull(decodedToken, "decodedToken is null");
            // 获取 Firebase 用户信息
            UserRecord userRecord = FirebaseAuth.getInstance().getUser(decodedToken.getUid());
            Assert.notNull(userRecord, "userRecord is null");

            ThirdAccountDto thirdAccountDto = new ThirdAccountDto();
            thirdAccountDto.setThirdId(getThirdId(userRecord));
            thirdAccountDto.setEmail(getUserEmail(userRecord));
            thirdAccountDto.setNickName(userRecord.getDisplayName());
            thirdAccountDto.setAvatar(userRecord.getPhotoUrl());
            return thirdAccountDto;

        } catch (Exception e) {
            log.error("Invalid Firebase ID token: {}", e.getMessage(), e);
            throw new RuntimeException("Invalid Firebase ID token: " + e.getMessage(), e);
        }
    }

    private String getUserEmail(UserRecord userRecord) {
        // 遍历 providers 列表，找到第一个非空的 email
        for (UserInfo userInfo : userRecord.getProviderData()) {
            if (StringUtils.isNotBlank(userInfo.getEmail())) {
                return userInfo.getEmail();
            }
        }

        // 如果 providers 中没有可用的 email，再尝试使用最外层的 email
        return userRecord.getEmail();
    }

    private String getThirdId(UserRecord userRecord) {
        // 遍历 providers 列表，找到第一个非空的 email
        for (UserInfo userInfo : userRecord.getProviderData()) {
            if (StringUtils.isNotBlank(userInfo.getUid())) {
                return userInfo.getUid();
            }
        }

        return null;
    }
}
