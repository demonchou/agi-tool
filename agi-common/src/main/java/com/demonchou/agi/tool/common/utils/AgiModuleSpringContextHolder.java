package com.demonchou.agi.tool.common.utils;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * SpringContextHolder
 *
 * @author demonchou
 * @version AgiModuleSpringContextHolder, 2025/8/13 17:11 demonchou
 */
public class AgiModuleSpringContextHolder implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    /**
     * 取得存储在静态变量中的ApplicationContext.
     */
    public static ApplicationContext getApplicationContext() {
        checkApplicationContext();
        return applicationContext;
    }

    /**
     * 实现ApplicationContextAware接口的context注入函数, 将其存入静态变量.
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        AgiModuleSpringContextHolder.applicationContext = applicationContext;
        System.out.println(">>> applicationContext.getBeanDefinitionNames().length:" + applicationContext.getBeanDefinitionNames().length);
    }

    /**
     * 从静态变量ApplicationContext中取得Bean, 自动转型为所赋值对象的类型.
     */
    public static <T> T getBean(String name) {
        checkApplicationContext();
        return (T) applicationContext.getBean(name);
    }

    public static <T> T getBean(Class<T> beanCalzz) {
        checkApplicationContext();
        return (T) applicationContext.getBean(beanCalzz);
    }

    private static void checkApplicationContext() {
        if (applicationContext == null) {
            throw new IllegalStateException("applicationContext未注入");
        }
    }
}
