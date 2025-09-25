package com.demonchou.agi.tool.third.login.processor;


import com.demonchou.agi.tool.third.login.dto.ThirdLoginRequestDto;
import com.demonchou.agi.tool.third.login.dto.ThirdAccountDto;
import com.demonchou.agi.tool.third.login.enums.UserChannelEnum;

/**
 * 登录处理器
 *
 * @author demonchou
 * @version LoginProcessor, 2025/8/11 16:45 demonchou
 */
public interface LoginProcessor {

    UserChannelEnum getUserChannel();

    ThirdAccountDto process(ThirdLoginRequestDto thirdLoginRequestDTO);
}
