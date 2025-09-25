package com.demonchou.agi.tool.pay.app.autoconfigure;

import com.demonchou.agi.tool.pay.app.service.PurchaseAppService;
import com.demonchou.agi.tool.pay.domain.processor.purchase.AppleInAppPayProcessor;
import com.demonchou.agi.tool.pay.domain.processor.purchase.GoogleInAppPayProcessor;
import com.demonchou.agi.tool.pay.domain.processor.purchase.PurchaseProcessorFactory;
import com.demonchou.agi.tool.pay.domain.service.impl.ApplePayServiceImpl;
import com.demonchou.agi.tool.pay.domain.service.impl.GooglePayServiceImpl;
import com.demonchou.agi.tool.pay.infra.adapter.impl.AppleStoreAdapterImpl;
import com.demonchou.agi.tool.pay.infra.adapter.impl.GooglePlayAdapterImpl;
import com.demonchou.agi.tool.pay.infra.persistence.repository.impl.OrderRepositoryImpl;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * 支付模块自动配置
 *
 * @author demonchou
 * @version PayAutoConfiguration, 2025/8/31 17:45 demonchou
 */
@Slf4j
@Configuration
@ComponentScan(basePackages = "com.demonchou.agi.module.pay")
@MapperScan("com.demonchou.agi.module.pay.infra.persistence.mapper")
public class PayAutoConfiguration {

    @Bean
    @ConditionalOnProperty(name = "agi-tool.pay.apple-pay.enabled", havingValue = "true")
    public AppleStoreAdapterImpl appleStoreAdapter(@Value("${agi-tool.pay.apple-pay.keyId}") String keyId,
                                                   @Value("${agi-tool.pay.apple-pay.issuerId}") String issuerId,
                                                   @Value("${agi-tool.pay.apple-pay.bundleId}") String bundleId,
                                                   @Value("${agi-tool.pay.apple-pay.environment}") String environment,
                                                   @Value("${agi-tool.pay.apple-pay.appleId}") Long appleId,
                                                   @Value("${agi-tool.pay.apple-pay.apiKeyPath}") String apiKeyPath,
                                                   @Value("${agi-tool.pay.apple-pay.rootCaPath}") String rootCaPath) {
        System.out.println(">>> AppleStoreAdapterImpl loaded");
        return new AppleStoreAdapterImpl(keyId, issuerId, bundleId, environment, appleId, apiKeyPath, rootCaPath);
    }

    @Bean
    @ConditionalOnProperty(name = "agi-tool.pay.google-pay.enabled", havingValue = "true")
    public GooglePlayAdapterImpl googlePlayAdapter(@Value("${agi-tool.pay.google-pay.proxy.address:}") String proxyAddress,
                                                   @Value("${agi-tool.pay.google-pay.proxy.port:}") Integer port,
                                                   @Value("${agi-tool.pay.google-pay.packageName:}") String googlePlayPackageName,
                                                   @Value("${agi-tool.pay.google-pay.serviceAccountFileName:}") String serviceAccountFileName) {
        System.out.println(">>> GooglePlayAdapterImpl loaded");
        return new GooglePlayAdapterImpl(proxyAddress, port, googlePlayPackageName, serviceAccountFileName);
    }

    @Bean
    @ConditionalOnProperty(name = "agi-tool.pay.apple-pay.enabled", havingValue = "true")
    public AppleInAppPayProcessor appleInAppPayProcessor() {
        System.out.println(">>> AppleInAppPayProcessor loaded");
        return new AppleInAppPayProcessor();
    }

    @Bean
    @ConditionalOnProperty(name = "agi-tool.pay.google-pay.enabled", havingValue = "true")
    public GoogleInAppPayProcessor googleInAppPayProcessor() {
        System.out.println(">>> GoogleInAppPayProcessor loaded");
        return new GoogleInAppPayProcessor();
    }

    @Bean
    @ConditionalOnExpression("${agi-tool.pay.google-pay.enabled:false} || ${agi-tool.pay.apple-pay.enabled:false}")
    public PurchaseProcessorFactory purchaseProcessorFactory() {
        System.out.println(">>> PurchaseProcessorFactory loaded");
        return new PurchaseProcessorFactory();
    }

    @Bean
    @ConditionalOnExpression("${agi-tool.pay.google-pay.enabled:false} || ${agi-tool.pay.apple-pay.enabled:false}")
    public OrderRepositoryImpl orderRepository() {
        System.out.println(">>> OrderRepository loaded");
        return new OrderRepositoryImpl();
    }

    @Bean
    @ConditionalOnExpression("${agi-tool.pay.google-pay.enabled:false} || ${agi-tool.pay.apple-pay.enabled:false}")
    @ConditionalOnBean(PurchaseProcessorFactory.class)
    public PurchaseAppService purchaseAppService() {
        System.out.println(">>> PurchaseAppService loaded");
        return new PurchaseAppService();
    }

    @Bean
    @ConditionalOnProperty(name = "agi-tool.pay.apple-pay.enabled", havingValue = "true")
    public ApplePayServiceImpl applePayService() {
        System.out.println(">>> ApplePayServiceImpl loaded");
        return new ApplePayServiceImpl();
    }

    @Bean
    @ConditionalOnProperty(name = "agi-tool.pay.google-pay.enabled", havingValue = "true")
    public GooglePayServiceImpl googlePayService() {
        System.out.println(">>> GooglePayServiceImpl loaded");
        return new GooglePayServiceImpl();
    }

}
