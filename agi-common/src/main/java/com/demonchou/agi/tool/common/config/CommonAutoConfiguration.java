package com.demonchou.agi.tool.common.config;

import com.demonchou.agi.tool.common.utils.ProxyIp;
import com.demonchou.agi.tool.common.utils.AgiModuleSpringContextHolder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author demonchou
 * @version CommonAutoConfiguration, 2025/8/20 11:46 demonchou
 */
@Configuration
@ComponentScan(basePackages = {"com.demonchou.agi.module"})
public class CommonAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public AgiModuleSpringContextHolder agiModuleSpringContextHolder() {
        System.out.println(">>> agiModuleSpringContextHolder loaded");
        return new AgiModuleSpringContextHolder();
    }

    @Bean
    @ConditionalOnProperty(prefix = "agi-tool.proxy", name = "enabled", havingValue = "true")
    @ConfigurationProperties(prefix = "agi-tool.proxy")
    public ProxyIpProperties proxyIpProperties() {
        System.out.println(">>> ProxyIpProperties loaded");
        return new ProxyIpProperties();
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "agi-tool.proxy", name = "enabled", havingValue = "true")
    public ProxyIp proxyIp(ProxyIpProperties properties) {
        System.out.println(">>> ProxyIp loaded");
        return new ProxyIp(properties);
    }
}
