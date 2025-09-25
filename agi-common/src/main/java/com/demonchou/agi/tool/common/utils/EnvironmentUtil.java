package com.demonchou.agi.tool.common.utils;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.core.env.Environment;

import java.util.Arrays;
import java.util.List;

/**
 * 环境工具类
 * @author demonchou
 * @version EnvironmentUtil, 2025/8/13 17:09 demonchou
 */
public class EnvironmentUtil {
    //线上环境包括prod和pre
    public static boolean isOnline() {
        return "prod".equalsIgnoreCase(getEnv()) || "pre".equalsIgnoreCase(getEnv());
    }

    public static boolean isPre() {
        return "pre".equalsIgnoreCase(getEnv());
    }

    public static boolean isLocal() {
        return "local".equalsIgnoreCase(getEnv());
    }

    public static boolean isLocalOrTest() {
        return isLocal() || "test".equalsIgnoreCase(getEnv());
    }

    public static boolean isProd() {
        return "prod".equalsIgnoreCase(getEnv());
    }

    private static String getEnv() {
        Environment env = AgiModuleSpringContextHolder.getBean(Environment.class);
        List<String> profiles = Arrays.asList(env.getActiveProfiles());
        return CollectionUtils.isEmpty(profiles) ? "test" : profiles.get(0);
    }
}
