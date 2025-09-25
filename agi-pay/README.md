# 支付模块

本模块提供移动应用内购买功能支持，目前已实现以下支付渠道：

- **Google支付**：支持Google Play应用内购买功能
- **Apple支付**：支持App Store应用内购买功能

## 快速集成

### 1. 添加依赖

在你的应用的`pom.xml`中添加以下依赖：

```xml

<dependency>
    <groupId>com.demonchoucom.demonchou</groupId>
    <artifactId>agi-pay</artifactId>
    <version>0.0.1-SNAPSHOT</version>
</dependency>
```

### 2. 配置支付模块

模块通过Spring Boot的自动配置机制加载服务，无需额外添加注解。在`application.yml`中添加以下配置：

```yaml
agi-tool:
  pay:
    apple-pay:
      enabled: true
      keyId: your-apple-key-id
      issuerId: your-apple-issuer-id
      bundleId: your-app-bundle-id
      environment: Sandbox  # 或 Production
      appleId: your-apple-id
      apiKeyPath: /path/to/your/apple/private/key.p8
      rootCaPath: /path/to/your/apple/root/ca.cer
    google-pay:
      enabled: true
      proxy:
        address: your-proxy-address  # 可选
        port: your-proxy-port        # 可选
      packageName: your-app-package-name
      serviceAccountFileName: /path/to/your/google/service-account.json
```

### 3. 注入并使用服务

```java
import com.demonchou.agi.tool.pay.app.service.PurchaseAppService;
import com.demonchou.agi.tool.pay.app.dto.CreateOrderReqDto;
import com.demonchou.agi.tool.pay.app.dto.CreateOrderResDto;
import com.demonchou.agi.tool.pay.app.dto.ConfirmPurchaseReqDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PaymentController {

    @Autowired
    private PurchaseAppService purchaseAppService;

    @PostMapping("/api/payment/create")
    public CreateOrderResDto createOrder(@RequestBody CreateOrderReqDto createOrderReqDto) {
        return purchaseAppService.purchase(createOrderReqDto);
    }

    @PostMapping("/api/payment/confirm")
    public boolean confirmPurchase(@RequestBody ConfirmPurchaseReqDto confirmPurchaseReqDto) {
        return purchaseAppService.confirm(confirmPurchaseReqDto);
    }
}
```

### 4. 配置支付回调接口

配置Google和Apple的支付回调接口，用于接收支付平台的通知：

```java
import com.demonchou.agi.tool.pay.app.service.WebhookAppService;
import com.demonchou.agi.tool.pay.domain.enums.PayChannelEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("api/payment/webhook")
public class PaymentWebhookController {

    @Autowired
    private WebhookAppService webhookService;

    @PostMapping("apple")
    public void appleWebhook(HttpServletRequest request) {
        webhookService.webhook(request, PayChannelEnum.APPLE_PAY);
    }

    @PostMapping("google")
    public void googleWebhook(HttpServletRequest request) {
        webhookService.webhook(request, PayChannelEnum.GOOGLE_PAY);
    }
}
```

## 请求参数说明

### CreateOrderReqDto 参数说明

| 参数名 | 类型 | 说明 | 必填 |
|--------|------|------|------|
| payChannel | String | 支付渠道，枚举值：GOOGLE_PAY、APPLE_PAY | 是 |
| productType | String | 产品类型，枚举值：SUBSCRIPTION（订阅）、INAPP（一次性付费） | 是 |
| platformProductId | String | 平台产品ID | 是 |
| userId | Long | 用户ID | 是 |
| goodsId | Long | 商品ID | 是 |
| goodsName | String | 商品名称 | 是 |
| goodsType | String | 商品类型 | 是 |
| quantity | int | 商品数量 | 是 |
| price | Long | 价格 | 是 |
| additionalInfo | Map<String, Object> | 附加信息 | 否 |

### ConfirmPurchaseReqDto 参数说明

| 参数名 | 类型 | 说明 | 必填 |
|--------|------|------|------|
| payChannel | String | 支付渠道，枚举值：GOOGLE_PAY、APPLE_PAY | 是 |
| productType | String | 产品类型，枚举值：SUBSCRIPTION（订阅）、INAPP（一次性付费） | 是 |

## 自动配置机制

本模块使用Spring Boot的自动配置机制，加载`PayAutoConfiguration`类，该类负责：

1. 扫描`com.demonchou.agi.tool.pay`包下的组件
2. 根据配置条件自动创建Apple和Google支付处理器
3. 创建`PurchaseProcessorFactory`工厂类来管理各支付处理器
4. 创建并注册`PurchaseAppService`和`WebhookAppService`服务实现

## 故障排查

如果无法自动注入服务，请确保：

1. 在配置中正确启用了相应的支付功能（例如`agi-tool.pay.apple-pay.enabled=true`）
2. 提供了必要的配置参数（如keyId、bundleId等）
3. 检查应用日志中是否有相关错误信息

若仍有问题，可以检查Spring Bean是否正确加载：

```java
// A在应用启动时打印所有Bean
@SpringBootApplication
public class YourApplication {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(YourApplication.class, args);
        String[] beanNames = context.getBeanDefinitionNames();
        Arrays.sort(beanNames);
        for (String beanName : beanNames) {
            System.out.println(beanName);
        }
    }
}
```