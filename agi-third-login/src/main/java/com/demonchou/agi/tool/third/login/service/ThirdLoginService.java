package com.demonchou.agi.tool.third.login.service;

import com.demonchou.agi.tool.third.login.dto.ThirdAccountDto;
import com.demonchou.agi.tool.third.login.dto.ThirdLoginRequestDto;

/**
 * @author demonchou
 * @version ThirdLoginService, 2025/8/20 09:33 demonchou
 */
public interface ThirdLoginService {

    ThirdAccountDto login(ThirdLoginRequestDto thirdLoginRequestDto);
}
