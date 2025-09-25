package com.demonchou.agi.tool.third.login.annotation;

import com.demonchou.agi.tool.third.login.autoconfigure.ThirdLoginAutoConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 启用第三方登录功能的注解
 * 在Spring Boot应用的启动类或配置类上添加此注解可启用第三方登录功能
 *
 * 使用示例:
 * <pre>
 * {@code @SpringBootApplication}
 * {@code @EnableThirdLogin}
 * public class MyApplication {
 *     public static void main(String[] args) {
 *         SpringApplication.run(MyApplication.class, args);
 *     }
 * }
 * </pre>
 *
 * @author demonchou
 * @version EnableThirdLogin, 2025/8/20
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(ThirdLoginAutoConfiguration.class)
public @interface EnableThirdLogin {
}