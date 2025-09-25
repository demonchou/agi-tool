package com.demonchou.agi.tool.third.login.service;

import com.alibaba.fastjson2.JSON;
import com.demonchou.agi.tool.third.login.BaseTest;
import com.demonchou.agi.tool.third.login.dto.ThirdAccountDto;
import com.demonchou.agi.tool.third.login.dto.ThirdLoginRequestDto;
import com.demonchou.agi.tool.third.login.enums.UserChannelEnum;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertNotNull;

/**
 * @author demonchou
 * @version ThirdLoginServiceTest, 2025/8/18 21:00 demonchou
 */
public class ThirdLoginServiceTest extends BaseTest {

    @Autowired
    private ThirdLoginService thirdLoginService;

    @Test
    public void login() {

        // 创建测试数据，但不执行实际的HTTP调用
        ThirdLoginRequestDto thirdLoginRequestDto = new ThirdLoginRequestDto();
        thirdLoginRequestDto.setUserChannel(UserChannelEnum.APPLE.getCode());
        thirdLoginRequestDto.setIdentityToken("eyJraWQiOiJTZjJsRnF3a3BYIiwiYWxnIjoiUlMyNTYifQ" +
                ".eyJpc3MiOiJodHRwczovL2FwcGxlaWQuYXBwbGUuY29tIiwiYXVkIjoiY29tLm15bG92ZXJzZS5zdG9yeS5jaGFwdGVyLmFpIiwiZXhwIjoxNzU1OTQzMDEyLCJpYXQiOjE3NTU4NTY2MTIsInN1YiI6IjAwMDg0Ny5iYWU2NzVjY2YxMGQ0MGE2YjQ0YWE2MDI3YTcwMmRkZC4wMjM2Iiwibm9uY2UiOiIzYjJlZmRmZjIyODI3ZWVkODg3ZGRlMDQxMDg5ZTRjYzdjMGNkNjY2NTY1OGM5Nzk1N2VkMmQ3MTBhYWY0ZDFlIiwiY19oYXNoIjoiUVNmOHRUTF9wSkdkZ1MzQjhGNXF0ZyIsImVtYWlsIjoiN254N2g4cmJyaEBwcml2YXRlcmVsYXkuYXBwbGVpZC5jb20iLCJlbWFpbF92ZXJpZmllZCI6dHJ1ZSwiaXNfcHJpdmF0ZV9lbWFpbCI6dHJ1ZSwiYXV0aF90aW1lIjoxNzU1ODU2NjEyLCJub25jZV9zdXBwb3J0ZWQiOnRydWV9.V4DkeA2qxCyU1_yc-wPUTpXYw_56tuKn54QMgWVXp0vnQD_x61GZhyfpEDVEAMieMwyfUuG_Rng3ICqfSEmeroiRmiW92DOyPwAYXY0qzQ3hdlOUPRnGCqOuiTYMZhiyjmP_SIiM29qu-RDImKJmpX1-9L2pVaF-KNd2gNU-NnRn5kG0oFETKtsIn9IRgZT0Y3eNU1P0I1Xr3RCQOVJucHHoKkCMmqAVSiP110-_gjxckQYzY2Dgnm2hRYKJjlr5eo1LB4gXjnTjI2GRjjvsU8vTldCrKdA8q4fDAZ6EqcgGY1VAwkRA0C1r6o2ZuEXjT-zEvBvviyogHno1_2sgSQ");
        ThirdAccountDto result = thirdLoginService.login(thirdLoginRequestDto);
        System.out.println(JSON.toJSONString(result));

        // 简单验证方法是否正常执行，不报错
        assertNotNull(result);
    }
}