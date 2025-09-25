package com.demonchou.agi.tool.third.login.autoconfigure;

import com.demonchou.agi.tool.common.utils.AgiModuleSpringContextHolder;
import com.demonchou.agi.tool.third.login.processor.AppleLoginProcessor;
import com.demonchou.agi.tool.third.login.processor.FacebookLoginProcessor;
import com.demonchou.agi.tool.third.login.processor.GoogleLoginProcessor;
import com.demonchou.agi.tool.third.login.processor.ThirdLoginProcessorFactory;
import com.demonchou.agi.tool.third.login.service.ThirdLoginService;
import com.demonchou.agi.tool.third.login.service.impl.ThirdLoginServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * 第三方登录自动配置类
 * 使用该类启用第三方登录功能的自动配置
 *
 * @author demonchou
 * @version ThirdLoginAutoConfiguration, 2025/8/19
 */
@Slf4j
@Configuration
@ComponentScan(basePackages = "com.demonchou.agi.module.third.login")
public class ThirdLoginAutoConfiguration {
    public ThirdLoginAutoConfiguration() {
        System.out.println(">>> ThirdLoginAutoConfiguration loaded");
    }

    @Bean
    @ConditionalOnProperty(name = "agi-tool.third-login.apple.enabled", havingValue = "true")
    public AppleLoginProcessor appleLoginProcessor() {
        AppleLoginProcessor appleLoginProcessor = new AppleLoginProcessor();
        System.out.println(">>> AppleLoginProcessor loaded.");
        return appleLoginProcessor;
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(name = "agi-tool.third-login.google.enabled", havingValue = "true")
    public GoogleLoginProcessor googleLoginProcessor(
            @Value("${agi-tool.third-login.google.googleClientId}") String androidGoogleClientId,
            @Value("${agi-tool.third-login.google.proxy.address:}") String proxyAddress,
            @Value("${agi-tool.third-login.google.proxy.port:}") Integer port) {
        GoogleLoginProcessor googleLoginProcessor = new GoogleLoginProcessor(
                androidGoogleClientId,
                proxyAddress,
                port
        );
        System.out.println(">>> GoogleLoginProcessor loaded");
        return googleLoginProcessor;
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(name = "agi-tool.third-login.facebook.enabled", havingValue = "true")
    public FacebookLoginProcessor facebookLoginProcessor(
            @Value("${agi-tool.third-login.facebook.appId}") String appId,
            @Value("${agi-tool.third-login.facebook.appSecret}") String appSecret,
            @Value("${agi-tool.third-login.facebook.firebaseServiceAccountFilePath}") String firebaseServiceAccountFilePath) {
        FacebookLoginProcessor facebookLoginProcessor = new FacebookLoginProcessor(
                appId,
                appSecret,
                firebaseServiceAccountFilePath
        );
        System.out.println(">>> FacebookLoginProcessor loaded");
        return facebookLoginProcessor;
    }

    @Bean
    @ConditionalOnBean(AgiModuleSpringContextHolder.class)
    @ConditionalOnMissingBean
    @ConditionalOnExpression("${agi-tool.third-login.google.enabled:false} || ${agi-tool.third-login.apple.enabled:false} " +
            "|| ${agi-tool.third-login.facebook.enabled:false}")
    public ThirdLoginProcessorFactory thirdLoginProcessorFactory() {
        System.out.println(">>> ThirdLoginProcessorFactory loaded");
        return new ThirdLoginProcessorFactory();
    }

    @Bean
    @ConditionalOnBean(ThirdLoginProcessorFactory.class)
    @ConditionalOnMissingBean(ThirdLoginService.class)
    @ConditionalOnExpression("${agi-tool.third-login.google.enabled:false} || ${agi-tool.third-login.apple.enabled:false} " +
            "|| ${agi-tool.third-login.facebook.enabled:false}")
    public ThirdLoginService thirdLoginService(ThirdLoginProcessorFactory factory) {
        System.out.println(">>> ThirdLoginService loaded");
        return new ThirdLoginServiceImpl(factory);
    }
}