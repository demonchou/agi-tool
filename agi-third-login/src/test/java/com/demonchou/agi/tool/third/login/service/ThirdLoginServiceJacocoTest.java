package com.demonchou.agi.tool.third.login.service;

import com.demonchou.agi.tool.third.login.BaseJacocoTest;
import com.demonchou.agi.tool.third.login.dto.ThirdLoginRequestDto;
import com.demonchou.agi.tool.third.login.dto.ThirdAccountDto;
import com.demonchou.agi.tool.third.login.enums.UserChannelEnum;
import com.demonchou.agi.tool.third.login.processor.LoginProcessor;
import com.demonchou.agi.tool.third.login.processor.ThirdLoginProcessorFactory;
import com.demonchou.agi.tool.third.login.service.impl.ThirdLoginServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * UserLoginService单元测试
 * 
 * @author demonchou
 * @version ThirdLoginServiceJacocoTest, 2025/8/13 15:32 demonchou
 */
class ThirdLoginServiceJacocoTest extends BaseJacocoTest {

    @InjectMocks
    private ThirdLoginServiceImpl thirdLoginService;

    @Mock
    private ThirdLoginProcessorFactory thirdLoginProcessorFactory;
    
    @Mock
    private LoginProcessor loginProcessor;

    @Test
    void login_success() {
        // 准备测试数据
        ThirdLoginRequestDto thirdLoginRequestDto = new ThirdLoginRequestDto();
        thirdLoginRequestDto.setUserChannel(UserChannelEnum.GOOGLE.getCode());
        thirdLoginRequestDto.setAccessToken("test-access-token");

        ThirdAccountDto expectedResult = new ThirdAccountDto("avatar-url", "test-user", "test@example.com");
        
        // 配置Mock行为
        when(thirdLoginProcessorFactory.getProcessor(UserChannelEnum.GOOGLE)).thenReturn(loginProcessor);
        when(loginProcessor.process(thirdLoginRequestDto)).thenReturn(expectedResult);
        
        // 执行测试
        ThirdAccountDto result = thirdLoginService.login(thirdLoginRequestDto);
        
        // 验证结果
        assertNotNull(result);
        assertEquals("avatar-url", result.getAvatar());
        assertEquals("test-user", result.getNickName());
        assertEquals("test@example.com", result.getEmail());
        
        // 验证调用
        verify(thirdLoginProcessorFactory).getProcessor(UserChannelEnum.GOOGLE);
        verify(loginProcessor).process(thirdLoginRequestDto);
    }
    
    @Test
    void login_nullLoginRequestDto() {
        // 执行测试并验证异常
        assertThrows(IllegalArgumentException.class, () -> thirdLoginService.login(null));
    }
    
    @Test
    void login_nullUserChannel() {
        // 准备测试数据
        ThirdLoginRequestDto thirdLoginRequestDto = new ThirdLoginRequestDto();
        // userChannel 为 null
        
        // 执行测试并验证异常
        assertThrows(IllegalArgumentException.class, () -> thirdLoginService.login(thirdLoginRequestDto));
    }
}