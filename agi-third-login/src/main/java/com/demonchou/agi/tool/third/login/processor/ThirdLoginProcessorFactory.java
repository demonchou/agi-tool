package com.demonchou.agi.tool.third.login.processor;

import com.demonchou.agi.tool.third.login.enums.UserChannelEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 登录处理器工厂类
 *
 * @author demonchou
 * @version LoginProcessorFactory, 2025/8/11 20:27 demonchou
 */
@Slf4j
@Service
public class ThirdLoginProcessorFactory implements InitializingBean {
    @Autowired
    private List<LoginProcessor> loginProcesses;
    private Map<UserChannelEnum, LoginProcessor> loginProcessorMap;

    public LoginProcessor getProcessor(UserChannelEnum userChannel) {
        return loginProcessorMap.get(userChannel);
    }


    @Override
    public void afterPropertiesSet() {
        // 初始化登录处理器映射表
        loginProcessorMap = loginProcesses.stream()
                .collect(Collectors.toMap(LoginProcessor::getUserChannel, Function.identity()));

        if (loginProcessorMap.isEmpty()) {
            log.error("登录处理器列表为空");
            throw new RuntimeException("登录处理器列表为空");
        }

        log.info("===== 登录处理器映射初始化完毕, 数量: {} =====", loginProcessorMap.size());
    }
}
