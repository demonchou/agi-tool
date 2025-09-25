package com.demonchou.agi.tool.common.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.entity.UrlEncodedFormEntity;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.core5.http.NameValuePair;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.message.BasicNameValuePair;
import org.apache.hc.core5.http.protocol.HttpContext;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * http请求工具类
 *
 * @author demonchou
 * @version HttpUtil, 2025/8/12 11:01 demonchou
 */
public class HttpUtil {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(HttpUtil.class);
    //声明一个httpClient连接管理器
    public static PoolingHttpClientConnectionManager cm;
    //创建请求配置对象
    public static RequestConfig config;
    //代理对象集合
    private static final List<String> userAgentList;

    private static final CloseableHttpClient noProxyClient;

    static {
        cm = new PoolingHttpClientConnectionManager();
        //设置最大连接数
        cm.setMaxTotal(200);
        //设置每个主机的最大并发
        cm.setDefaultMaxPerRoute(20);

        config = RequestConfig.custom()
                //设置创建连接超时时间
                .setConnectTimeout(org.apache.hc.core5.util.Timeout.ofMilliseconds(2000))
                //设置连接超时时间
                .setResponseTimeout(org.apache.hc.core5.util.Timeout.ofMilliseconds(2000))
                //设置请求超时时间
                .setConnectionRequestTimeout(org.apache.hc.core5.util.Timeout.ofMilliseconds(5000))
                .build();
        userAgentList = new ArrayList<>();
        userAgentList.add("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.132 Safari/537.36");
        userAgentList.add("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.15; rv:73.0) Gecko/20100101 Firefox/73.0");
        userAgentList.add("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_3) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/13.0.5 Safari/605.1.15");
        userAgentList.add("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36 Edge/16.16299");
        userAgentList.add("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.3578.98 Safari/537.36");
        userAgentList.add("Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:63.0) Gecko/20100101 Firefox/63.0");
        noProxyClient = createDefaultCloseableHttpClient();
    }

    private static CloseableHttpClient createDefaultCloseableHttpClient() {
        return HttpClients.custom().setConnectionManager(cm).build();
    }

    public static String getWithHeader(String url, Map<String, String> header) {
        HttpGet httpGet = new HttpGet(url);
        for (Map.Entry<String, String> entry : header.entrySet()) {
            httpGet.setHeader(entry.getKey(), entry.getValue());
        }
        httpGet.setConfig(config);
        try (CloseableHttpResponse response = noProxyClient.execute(httpGet, (HttpContext) null)) {
            //5. 获取响应内容
            if (response.getCode() == 200 && response.getEntity() != null) {
                return EntityUtils.toString(response.getEntity());
            }
        } catch (Exception e) {
            log.error("出现异常", e);
        }
        return null;
    }

    public static String postParam(String url, Map<String, String> param) {
        HttpPost httpPost = new HttpPost(url);
        List<NameValuePair> params = new ArrayList<>();
        //建立一个NameValuePair数组，用于存储欲传送的参数
        for (Map.Entry<String, String> entry : param.entrySet()) {
            params.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
        }

        //添加参数
        httpPost.setEntity(new UrlEncodedFormEntity(params, StandardCharsets.UTF_8));
        String contentType = "application/x-www-form-urlencoded";
        httpPost.setHeader("Content-type", contentType);
        httpPost.setConfig(config);
        try (CloseableHttpResponse response = noProxyClient.execute(httpPost, (HttpContext) null)) {
            log.debug("statusCode:{}", response.getCode());
            //5. 获取响应内容
            if (response.getCode() == 200 && response.getEntity() != null) {
                String responseStr = EntityUtils.toString(response.getEntity());
                if (StringUtils.isBlank(responseStr) || "null".equals(responseStr)) {
                    log.debug("响应状态码为 200 但响应内容为空");
                }
                return responseStr;
            } else {
                log.debug("statusCode:{}", response.getCode());
                try {
                    if (response.getEntity() != null) {
                        log.info("errorInfo:{}", EntityUtils.toString(response.getEntity()));
                    }
                } catch (Exception e) {
                    log.error("read error info error:", e);
                }
            }
        } catch (Exception e) {
            log.error("出现异常", e);
        }
        return null;
    }

    public static String get(String url) {
        HttpGet httpGet = new HttpGet(url);
        httpGet.setConfig(config);
        try (CloseableHttpResponse response = noProxyClient.execute(httpGet, (HttpContext) null)) {
            //5. 获取响应内容
            log.debug("get statuscode:{}", response.getCode());
            if (response.getCode() == 200 && response.getEntity() != null) {
                return EntityUtils.toString(response.getEntity());
            }
        } catch (Exception e) {
            log.error("出现异常", e);
        }
        return null;
    }
}
