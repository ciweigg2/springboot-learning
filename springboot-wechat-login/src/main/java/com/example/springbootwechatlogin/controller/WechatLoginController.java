package com.example.springbootwechatlogin.controller;

import com.example.springbootwechatlogin.dto.BindPhoneRequest;
import com.example.springbootwechatlogin.dto.LoginResponse;
import com.example.springbootwechatlogin.dto.MiniProgramLoginRequest;
import com.example.springbootwechatlogin.model.WechatSession;
import com.example.springbootwechatlogin.service.WechatApiService;
import com.example.springbootwechatlogin.service.WechatLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/wechat")
public class WechatLoginController {

    @Autowired
    private WechatApiService wechatApiService;

    @Autowired
    private WechatLoginService wechatLoginService;

    @PostMapping("/mini/login")
    public LoginResponse miniLogin(@RequestBody MiniProgramLoginRequest request) {
        WechatSession session = wechatApiService.miniProgramCode2Session(request.getCode(), request.getNickname());
        return wechatLoginService.login("mini_program", session);
    }

    @GetMapping("/official/auth-url")
    public Map<String, String> officialAuthUrl(@RequestParam(defaultValue = "demo-state") String state) {
        Map<String, String> result = new LinkedHashMap<String, String>();
        result.put("mockCallback", "http://localhost:8087/wechat/official/callback?code=mock-code-001&state=" + state);
        result.put("realUrlTemplate", "https://open.weixin.qq.com/connect/oauth2/authorize?appid=APPID&redirect_uri=REDIRECT_URI&response_type=code&scope=snsapi_userinfo&state=" + state + "#wechat_redirect");
        return result;
    }

    @GetMapping("/official/callback")
    public LoginResponse officialCallback(@RequestParam String code, @RequestParam(required = false) String state) {
        WechatSession session = wechatApiService.officialAccountOauth(code);
        return wechatLoginService.login("official_account", session);
    }

    @PostMapping("/bind-phone")
    public LoginResponse bindPhone(@RequestBody BindPhoneRequest request) {
        return wechatLoginService.bindPhone(request.getToken(), request.getPhone());
    }

    @GetMapping("/me")
    public Map<String, Object> me(@RequestHeader("token") String token) {
        return wechatLoginService.me(token);
    }
}
