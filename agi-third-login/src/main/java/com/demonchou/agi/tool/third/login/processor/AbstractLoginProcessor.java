package com.demonchou.agi.tool.third.login.processor;

import com.demonchou.agi.tool.third.login.dto.ThirdLoginRequestDto;
import com.demonchou.agi.tool.third.login.dto.ThirdAccountDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author demonchou
 * @version AbstractLoginProcessor, 2025/8/11 20:39 demonchou
 */
@Slf4j
@Service
public abstract class AbstractLoginProcessor implements LoginProcessor {

    // 参数校验
    abstract void checkParams(ThirdLoginRequestDto thirdLoginRequestDto);

    // 获取处理器
    abstract ThirdAccountDto login(ThirdLoginRequestDto thirdLoginRequestDto);


    @Override
    public ThirdAccountDto process(ThirdLoginRequestDto thirdLoginRequestDTO) {
        // 参数校验
        this.checkParams(thirdLoginRequestDTO);

        // 登录
        return this.login(thirdLoginRequestDTO);
    }
}
