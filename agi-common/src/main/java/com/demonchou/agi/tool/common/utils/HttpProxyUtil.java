package com.demonchou.agi.tool.common.utils;

import com.alibaba.fastjson2.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.hc.client5.http.auth.AuthScope;
import org.apache.hc.client5.http.auth.UsernamePasswordCredentials;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.entity.UrlEncodedFormEntity;
import org.apache.hc.client5.http.impl.auth.BasicCredentialsProvider;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.client5.http.impl.routing.DefaultProxyRoutePlanner;
import org.apache.hc.client5.http.routing.HttpRoutePlanner;
import org.apache.hc.core5.http.HttpHost;
import org.apache.hc.core5.http.NameValuePair;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.message.BasicNameValuePair;
import org.apache.hc.core5.util.Timeout;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * @author demonchou
 * @version HttpProxyUtil, 2025/8/13 17:20 demonchou
 */
@Slf4j
public class HttpProxyUtil {

    private static final CloseableHttpClient proxyClient;

    //声明一个httpClient连接管理器
    public static PoolingHttpClientConnectionManager cm;
    //创建请求配置对象
    public static RequestConfig config;
    //代理对象集合
    private static final List<String> userAgentList;
    public static BasicCredentialsProvider credentialsProvider;

    //静态代码块会在类进行加载的时候执行
    static {
        cm = new PoolingHttpClientConnectionManager();
        //设置最大连接数
        cm.setMaxTotal(200);
        //设置每个主机的最大并发
        cm.setDefaultMaxPerRoute(20);

        HttpHost proxy = selectRandomProxy();
        RequestConfig.Builder configBuilder = RequestConfig.custom()
                //设置创建连接超时时间
                .setConnectTimeout(Timeout.ofMilliseconds(1000))
                //设置连接超时时间
                .setResponseTimeout(Timeout.ofMilliseconds(1000))
                //设置请求超时时间
                .setConnectionRequestTimeout(Timeout.ofMilliseconds(1000));

        if (proxy != null) {
            configBuilder.setProxy(proxy);
            log.info("Request config initialized with proxy: {}:{}", proxy.getHostName(), proxy.getPort());
        } else {
            log.info("Request config initialized without proxy");
        }
        config = configBuilder.build();

        userAgentList = new ArrayList<>();
        userAgentList.add("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.132 Safari/537.36");
        userAgentList.add("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.15; rv:73.0) Gecko/20100101 Firefox/73.0");
        userAgentList.add("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_3) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/13.0.5 Safari/605.1.15");
        userAgentList.add("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36 Edge/16.16299");
        userAgentList.add("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.3578.98 Safari/537.36");
        userAgentList.add("Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:63.0) Gecko/20100101 Firefox/63.0");

        proxyClient = createCloseableHttpClient();
    }

    /**
     * 获取随机User-Agent
     *
     * @return 随机的User-Agent字符串
     */
    private static String getUserAgent() {
        return userAgentList.get(new Random().nextInt(userAgentList.size()));
    }

    /**
     * 创建支持代理的CloseableHttpClient实例
     */
    private static CloseableHttpClient createCloseableHttpClient() {
        HttpHost proxy = selectRandomProxy();

        // 创建HttpClient构建器
        org.apache.hc.client5.http.impl.classic.HttpClientBuilder builder = HttpClients.custom()
                .setConnectionManager(cm)
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setConnectTimeout(Timeout.ofMilliseconds(1000))
                        .setResponseTimeout(Timeout.ofMilliseconds(1000))
                        .setConnectionRequestTimeout(Timeout.ofMilliseconds(1000))
                        .build());

        // 如果有代理配置，则设置代理相关参数
        if (proxy != null) {
            configureCredentials(proxy);
            HttpRoutePlanner routePlanner = new DefaultProxyRoutePlanner(proxy);
            builder.setDefaultCredentialsProvider(credentialsProvider)
                    .setRoutePlanner(routePlanner);
            log.info("Configuring HttpClient with proxy: {}:{}", proxy.getHostName(), proxy.getPort());
        } else {
            log.info("Configuring HttpClient without proxy");
        }

        return builder.build();
    }

    /**
     * 随机选择一个代理服务器
     */
    private static HttpHost selectRandomProxy() {
        List<HttpHost> httpHosts = new ArrayList<>();

        String ip1 = ProxyIp.getIp1();
        Integer port1 = ProxyIp.getPort1();
        String ip2 = ProxyIp.getIp2();
        Integer port2 = ProxyIp.getPort2();

        if (ip1 != null && port1 != null) {
            httpHosts.add(new HttpHost(ip1, port1));
        }

        if (ip2 != null && port2 != null) {
            httpHosts.add(new HttpHost(ip2, port2));
        }

        if (httpHosts.isEmpty()) {
            // 如果没有配置代理，返回null
            log.warn("No proxy configured, not using proxy");
            return null;
        }

        return httpHosts.get(new Random().nextInt(httpHosts.size()));
    }

    /**
     * 根据选定的代理服务器配置认证信息
     */
    private static void configureCredentials(HttpHost proxy) {
        if (proxy == null) {
            log.warn("Cannot configure credentials for null proxy");
            return;
        }

        credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(
                new AuthScope(proxy.getHostName(), proxy.getPort()),
                new UsernamePasswordCredentials("admin", "123456".toCharArray()));
    }


    /**
     * 发送GET请求
     *
     * @param url 请求URL
     * @return 响应内容，如果请求失败返回null
     */
    public static String get(String url) {
        HttpGet httpGet = new HttpGet(url);
        // 设置请求配置
        httpGet.setConfig(config);
        // 设置随机User-Agent
        httpGet.setHeader("User-Agent", getUserAgent());

        try (CloseableHttpResponse response = proxyClient.execute(httpGet, (org.apache.hc.core5.http.protocol.HttpContext) null)) {
            log.info("发送GET请求: {}", url);
            int statusCode = response.getCode();

            // 获取响应内容
            if (statusCode == 200 && response.getEntity() != null) {
                String responseBody = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
                return responseBody;
            } else {
                log.warn("get request failed，code: {}", statusCode);
                if (response.getEntity() != null) {
                    String errorBody = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
                    log.warn("error msg: {}", errorBody);
                }
            }
        } catch (Exception e) {
            log.error("get request exception. URL:{}, errMsg: {}", url, e.getMessage(), e);
        }
        return null;
    }

    /**
     * 发送GET请求，支持自定义请求头
     *
     * @param url     请求URL
     * @param headers 自定义请求头
     * @return 响应内容，如果请求失败返回null
     */
    public static String getWithHeader(String url, Map<String, String> headers) {
        HttpGet httpGet = new HttpGet(url);
        // 设置请求配置
        httpGet.setConfig(config);
        // 设置随机User-Agent
        httpGet.setHeader("User-Agent", getUserAgent());

        // 添加自定义请求头
        if (headers != null && !headers.isEmpty()) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                httpGet.setHeader(entry.getKey(), entry.getValue());
            }
        }

        try (CloseableHttpResponse response = proxyClient.execute(httpGet, (org.apache.hc.core5.http.protocol.HttpContext) null)) {
            int statusCode = response.getCode();
            log.info("getWithHeader statusCode: {}", statusCode);
            // 获取响应内容
            if (statusCode == 200 && response.getEntity() != null) {
                return EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
            } else {
                log.warn("getWithHeader request failed，code: {}", statusCode);
                if (response.getEntity() != null) {
                    String errorBody = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
                    log.warn("error msg: {}", errorBody);
                }
            }
        } catch (Exception e) {
            log.error("getWithHeader request exception. URL: {}, errMsg: {}", url, e.getMessage(), e);
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

        try (CloseableHttpResponse response = proxyClient.execute(httpPost, (org.apache.hc.core5.http.protocol.HttpContext) null)) {
            int statusCode = response.getCode();
            log.warn("statusCode:{}", statusCode);
            //5. 获取响应内容
            if (statusCode == 200 && response.getEntity() != null) {
                String responseStr = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
                if (StringUtils.isBlank(responseStr) || "null".equals(responseStr)) {
                    log.warn("statusCode is 200, but response is null");
                }
                return responseStr;
            } else {
                log.warn("post request failed, code:{}", response.getCode());
                try {
                    if (response.getEntity() != null) {
                        log.info("errorInfo:{}", EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8));
                    }
                } catch (Exception e) {
                    log.error("read error info error:", e);
                }
            }
        } catch (Exception e) {
            log.error("post request exception. URL:{}, param:{}, errMsg: {}", url, JSON.toJSONString(param), e.getMessage(), e);
        }
        return null;
    }
}
