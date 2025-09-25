package com.demonchou.agi.tool.common.utils;

import com.demonchou.agi.tool.common.config.ProxyIpProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * 代理IP配置
 *
 * @author demonchou
 * @version ProxyIp, 2025/8/13 17:20 demonchou
 */
@Slf4j
@Component
public class ProxyIp {
    private final ProxyIpProperties properties;

    // 保持静态变量用于HttpProxyUtil访问
    private static String ip1;
    private static Integer port1;
    private static String ip2;
    private static Integer port2;

    public ProxyIp(ProxyIpProperties properties) {
        this.properties = properties;
    }

    @PostConstruct
    public void init() {
        updateFromProperties();
    }

    private void updateFromProperties() {
        ip1 = properties.getIp1();
        ip2 = properties.getIp2();
        port1 = properties.getPort1();
        port2 = properties.getPort2();
        log.info("ProxyIp initialized with values - ip1: {}, ip2: {}, port1: {}, port2: {}",
                ip1, ip2, port1, port2);
    }

    public static String getIp1() {
        return ip1;
    }

    public static Integer getPort1() {
        return port1;
    }

    public static String getIp2() {
        return ip2;
    }

    public static Integer getPort2() {
        return port2;
    }
}