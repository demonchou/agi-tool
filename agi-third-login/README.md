# 第三方登录模块

本模块提供第三方登录功能支持，目前已实现以下登录方式：

- **Google登录**：通过Google账号进行身份验证和登录
- **Apple登录**：支持Sign in with Apple功能
- **Facebook登录**：通过Facebook账号进行身份验证和登录

## 快速集成

### 1. 添加依赖

在你的应用的`pom.xml`中添加以下依赖：

```xml

<dependency>
    <groupId>com.demonchoucom.demonchou</groupId>
    <artifactId>agi-third-login</artifactId>
    <version>0.0.1-SNAPSHOT</version>
</dependency>
```

### 2. 启用第三方登录功能

在你的Spring Boot应用的主类或配置类上添加`@EnableThirdLogin`注解：

```java
import com.demonchou.agi.tool.third.login.annotation.EnableThirdLogin;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableThirdLogin  // 启用第三方登录功能
public class YourApplication {
    public static void main(String[] args) {
        SpringApplication.run(YourApplication.class, args);
    }
}
```

### 3. 注入并使用服务

```java
import com.demonchou.agi.tool.third.login.service.ThirdLoginService;
import com.demonchou.agi.tool.third.login.dto.ThirdLoginRequestDto;
import com.demonchou.agi.tool.third.login.dto.ThirdAccountDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {

    @Autowired
    private ThirdLoginService thirdLoginService;

    @PostMapping("/api/login/third")
    public ThirdAccountDto thirdLogin(@RequestBody ThirdLoginRequestDto requestDto) {
        return thirdLoginService.login(requestDto);
    }
}
```

## 配置选项

模块通过Spring Boot的自动配置机制加载服务，可通过以下方式进行配置：

### 在application.yml中添加第三方登录配置

以下是支持的所有配置选项，可根据需要进行设置：

```yaml
agi-tool:
  third-login:
    apple:
      enabled: true
    google:
      enabled: true
      googleClientId: your-google-client-id
    facebook:
      enabled: true
      appId: your-facebook-app-id
      appSecret: your-facebook-app-secret
  proxy:
    ip1: your-proxy-ip-address  # 可选，用于Google登录
    port1: your-proxy-port      # 可选，用于Google登录
```

### 配置说明

#### 第三方登录配置

- **Apple登录**
  - `agi-tool.third-login.apple.enabled`: 启用Apple登录（true/false）

- **Google登录**
  - `agi-tool.third-login.google.enabled`: 启用Google登录（true/false）
  - `agi-tool.third-login.google.googleClientId`: Google客户端ID

- **Facebook登录**
  - `agi-tool.third-login.facebook.enabled`: 启用Facebook登录（true/false）
  - `agi-tool.third-login.facebook.appId`: Facebook应用ID
  - `agi-tool.third-login.facebook.appSecret`: Facebook应用密钥

- **代理设置**（用于Google登录）
  - `agi-tool.proxy.ip1`: 代理服务器IP地址
  - `agi-tool.proxy.port1`: 代理服务器端口

## 请求参数说明

`ThirdLoginRequestDto` 参数说明：

| 参数名 | 类型 | 说明 | 必填 | 适用登录方式 |
|--------|------|------|------|------------|
| userChannel | String | 登录渠道，枚举值：GOOGLE、APPLE、FACEBOOK、GUEST | 是 | 所有 |
| identityToken | String | Apple登录返回的身份令牌 | 否 | Apple |
| code | String | Apple登录返回的授权码 | 否 | Apple |
| accessToken | String | Google或Facebook登录返回的访问令牌 | 否 | Google、Facebook |
| deviceId | String | 设备ID，用于游客登录 | 否 | GUEST |

`ThirdAccountDto` 返回数据说明：

| 字段名 | 类型 | 说明 |
|--------|------|------|
| avatar | String | 用户头像 URL |
| nickName | String | 用户昵称 |
| email | String | 用户邮箱 |
| thirdId | String | 第三方平台用户ID |

## 自动配置机制

本模块使用Spring Boot的自动配置机制，当添加`@EnableThirdLogin`注解时，会自动导入`ThirdLoginAutoConfiguration`类，该类负责：

1. 扫描`com.demonchou.agi.tool.third.login`包下的组件
2. 根据配置条件自动创建Apple、Google和Facebook登录处理器
3. 创建`ThirdLoginProcessorFactory`工厂类来管理各登录处理器
4. 创建并注册`ThirdLoginService`服务实现

## 故障排查

如果无法自动注入服务，请确保：

1. 已经正确添加了`@EnableThirdLogin`注解
2. 组件扫描路径正确（默认扫描`com.demonchou.agi.tool.third.login`包）
3. 在配置中正确启用了相应的第三方登录（例如`agi-tool.third-login.google.enabled=true`）
4. 提供了必要的配置参数（如clientId、appId等）

若仍有问题，可以手动导入配置类：

```java
import com.demonchou.agi.tool.third.login.autoconfigure.ThirdLoginAutoConfiguration;

@Import(ThirdLoginAutoConfiguration.class)
```