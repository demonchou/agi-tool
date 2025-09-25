package com.demonchou.agi.tool.third.login.service.impl;

import com.alibaba.fastjson2.JSON;
import com.demonchou.agi.tool.third.login.dto.ThirdAccountDto;
import com.demonchou.agi.tool.third.login.dto.ThirdLoginRequestDto;
import com.demonchou.agi.tool.third.login.enums.UserChannelEnum;
import com.demonchou.agi.tool.third.login.processor.LoginProcessor;
import com.demonchou.agi.tool.third.login.processor.ThirdLoginProcessorFactory;
import com.demonchou.agi.tool.third.login.service.ThirdLoginService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 * @author demonchou
 * @version ThirdLoginServiceImpl, 2025/8/20 09:38 demonchou
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ThirdLoginServiceImpl implements ThirdLoginService {

    private final ThirdLoginProcessorFactory thirdLoginProcessorFactory;
    @Override
    public ThirdAccountDto login(ThirdLoginRequestDto thirdLoginRequestDto) {
        log.info("用户登录请求：{}", JSON.toJSONString(thirdLoginRequestDto));
        Assert.notNull(thirdLoginRequestDto, "登录请求DTO不能为空");
        Assert.notNull(thirdLoginRequestDto.getUserChannel(), "用户渠道不能为空");
        UserChannelEnum userChannelEnum = UserChannelEnum.getByCode(thirdLoginRequestDto.getUserChannel());
        Assert.notNull(userChannelEnum, "用户渠道不支持");

        LoginProcessor processor = thirdLoginProcessorFactory.getProcessor(userChannelEnum);
        return processor.process(thirdLoginRequestDto);
    }
}
