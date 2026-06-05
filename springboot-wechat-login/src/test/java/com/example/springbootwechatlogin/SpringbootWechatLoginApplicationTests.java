package com.example.springbootwechatlogin;

import com.example.springbootwechatlogin.model.WechatSession;
import com.example.springbootwechatlogin.service.WechatApiService;
import com.example.springbootwechatlogin.service.WechatLoginService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringbootWechatLoginApplicationTests {

    @Autowired
    private WechatApiService wechatApiService;

    @Autowired
    private WechatLoginService wechatLoginService;

    @Test
    public void miniProgramMockLoginWorks() {
        WechatSession session = wechatApiService.miniProgramCode2Session("test-code", "测试用户");

        assertNotNull(wechatLoginService.login("mini_program", session).getToken());
        assertTrue(wechatLoginService.login("mini_program", session).getUserId() > 0);
    }
}
