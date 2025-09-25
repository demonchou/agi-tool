package com.demonchou.agi.tool.common.config;

import lombok.Data;
import org.springframework.stereotype.Component;

/**
 * @author demonchou
 * @version ProxyIpProperties, 2025/8/20 21:06 demonchou
 */
@Data
@Component
public class ProxyIpProperties {
    private String ip1;
    private Integer port1;
    private String ip2;
    private Integer port2;
}
