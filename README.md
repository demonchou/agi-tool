### agi相关公共功能模块 - 快速上手指南

## 项目概述
本项目是一个聊天相关的公共功能模块集合，提供了基础功能、支付和第三方登录等核心能力，采用Maven多模块架构，便于各模块独立开发和集成。

## 项目结构

- **agi-common**: 基础公共模块
- **agi-pay**: 支付模块
- **agi-third-login**: 第三方登录模块

## 技术栈与框架
- **构建工具**：Maven 4.0.0
- **开发语言**：Java 11
- **核心框架**：Spring Boot
- **第三方库**：
    - Hutool-all 5.8.24 (工具类库)
    - Apache Commons Lang3 3.18.0 (通用工具类)
    - Apache Commons IO 2.14.0 (IO操作工具)
    - Apache Commons Collections4 4.4 (集合工具)
    - Fastjson2 2.0.29 (JSON处理)
    - JJWT 0.9.1 (JWT令牌处理)
    - JWKS-RSA 0.9.0 (RSA密钥处理)
    - SLF4J 1.7.21 (日志框架)
    - Lombok 1.18.30 (减少样板代码)
- **特定功能库**：
    - app-store-server-library 2.2.0 (Apple支付)
    - google-api-services-androidpublisher v3 (Google支付)
    - google-api-client 1.33.0 (Google API客户端)
    - httpclient5 5.2.1 (HTTP客户端)
    - knife4j-spring-boot-starter 2.0.9 (API文档)

## 模块详情

### 1. agi-common 基础公共模块
**功能**：提供项目共用的基础组件和工具类
- 日志工具
- 缓存管理
- 通用工具类
- 枚举类定义
- 基础配置

### 2. agi-pay 支付模块
**功能**：提供支付相关能力
- 支持Apple、Google支付
- 支付回调通知处理
- 订单管理（包含退款、查询等计划中功能）

### 3. agi-third-login 第三方登录模块
**功能**：提供第三方登录能力
- 支持Apple、Google登录
- 返回第三方用户邮箱信息

## 快速接入指南

### 环境要求
- JDK 11或更高版本
- Maven 3.6.0或更高版本
- IDE (推荐IntelliJ IDEA或Eclipse)

### 构建项目
1. 克隆项目到本地
2. 进入项目根目录
3. 执行Maven构建命令
   ```bash
   mvn clean install
   ```

### 模块集成

#### 1. 集成agi-common模块
在需要集成的项目的pom.xml中添加依赖：

```xml

<dependency>
    <groupId>com.demonchoucom.demonchou</groupId>
    <artifactId>agi-common</artifactId>
    <version>0.0.1-SNAPSHOT</version>
</dependency>
```

#### 2. 集成agi-pay模块

```xml

<dependency>
    <groupId>com.demonchoucom.demonchou</groupId>
    <artifactId>agi-pay</artifactId>
    <version>0.0.1-SNAPSHOT</version>
</dependency>
```

#### 3. 集成agi-third-login模块

```xml

<dependency>
    <groupId>com.demonchoucom.demonchou</groupId>
    <artifactId>agi-third-login</artifactId>
    <version>0.0.1-SNAPSHOT</version>
</dependency>
```

### 配置说明
各模块的具体配置请参考对应模块的资源目录下的配置文件。核心配置项包括：
- 第三方服务的API密钥
- 回调URL配置
- 日志级别配置
- 缓存配置

### 使用示例

#### 第三方登录示例

```java
// 引入相关类

import com.demonchou.agi.tool.third.login.service.ThirdLoginService;

// 创建登录服务实例
ThirdLoginService loginService = new ThirdLoginService();

        // Google登录
        String googleToken = "user_google_token";
        Map<String, Object> googleUserInfo = loginService.googleLogin(googleToken);
        String email = (String) googleUserInfo.get("email");

        // Apple登录
        String appleToken = "user_apple_token";
        Map<String, Object> appleUserInfo = loginService.appleLogin(appleToken);
        String email = (String) appleUserInfo.get("email");
```

#### 支付示例

```java
// 引入相关类

import com.demonchou.agi.tool.pay.service.PayService;

// 创建支付服务实例
PayService payService = new PayService();

        // 创建Apple支付订单
        Map<String, Object> appleOrderParams = new HashMap<>();
appleOrderParams.

        put("productId","product_123");
appleOrderParams.

        put("userId","user_456");

        Map<String, Object> applePayResult = payService.createApplePayOrder(appleOrderParams);

        // 创建Google支付订单
        Map<String, Object> googleOrderParams = new HashMap<>();
googleOrderParams.

        put("productId","product_123");
googleOrderParams.

        put("userId","user_456");

        Map<String, Object> googlePayResult = payService.createGooglePayOrder(googleOrderParams);
```

## 注意事项
1. 确保所有依赖项版本匹配
2. 根据实际环境修改配置文件
3. 生产环境中建议关闭调试日志
4. 支付相关功能需要正确配置第三方平台的密钥和回调地址
5. 第三方登录需要确保应用已在对应平台注册并获得授权

## 后续计划
- 完善支付模块的订单查询和退款功能
- 增加更多第三方平台支持
- 优化性能和安全性
- 提供更详细的API文档