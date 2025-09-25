package com.demonchou.agi.tool.third.login.utils;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.auth0.jwk.InvalidPublicKeyException;
import com.auth0.jwk.Jwk;
import com.demonchou.agi.tool.common.utils.HttpUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.security.PublicKey;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

/**
 * apple 登录工具类
 *
 * @author demonchou
 * @version AppleLoginUtil, 2025/8/12 11:25 demonchou
 */
@Slf4j
public class AppleLoginUtil {

    public static Map<String, String> checkIdentityToken(String identityToken) throws InvalidPublicKeyException {
        String aud;
        String sub;
        JSONObject userInfo;
        if (identityToken.split("\\.").length > 1) {
            String claim = new String(Base64.getDecoder().decode(identityToken.split("\\.")[1]));
            userInfo = JSONObject.parseObject(claim);
            aud = userInfo.get("aud").toString();
            sub = userInfo.get("sub").toString();
            // 通过 HttpClient 请求获取的公钥 keys
            String appleKey = HttpUtil.get("https://appleid.apple.com/auth/keys");
            if (StringUtils.isEmpty(appleKey)) {
                log.warn("获取apple公钥失败");
                return null;
            }
            JSONObject jsonObject = JSON.parseObject(appleKey);
            assert jsonObject != null;
            JSONArray keys = jsonObject.getJSONArray("keys");
            for (int i = 0; i < keys.size(); i++) {
                Jwk jwa = Jwk.fromValues(keys.getJSONObject(i));
                PublicKey publicKey = jwa.getPublicKey();
                String result = verify(publicKey, identityToken, aud, sub);
                if ("success".equals(result)) {
                    Map<String, String> map = new HashMap<>();
                    map.put("appleId", sub);
                    map.put("email", userInfo.getString("email"));
                    map.put("is_private_email", "true");
                    if (!userInfo.containsKey("is_private_email") || !userInfo.getBooleanValue("is_private_email")) {
                        map.put("is_private_email", "false");
                    }
                    return map;
                }
            }
        }
        throw new RuntimeException("Auth apple token check fail");
    }

    public static String verify(PublicKey key, String jwt, String audience, String subject) {
        JwtParser jwtParser = Jwts.parser().setSigningKey(key);
        jwtParser.requireIssuer("https://appleid.apple.com");
        jwtParser.requireAudience(audience);
        jwtParser.requireSubject(subject);
        try {
            Jws<Claims> claim = jwtParser.parseClaimsJws(jwt);
            if (claim != null && claim.getBody().containsKey("auth_time")) {
                return "success";
            }
            return "failed";
        } catch (ExpiredJwtException e) {
            log.error("token超时,请重新点击", e);
            return "failed";
        } catch (Exception e) {
            log.error("token解析失败", e);
            return "failed";
        }
    }
}
