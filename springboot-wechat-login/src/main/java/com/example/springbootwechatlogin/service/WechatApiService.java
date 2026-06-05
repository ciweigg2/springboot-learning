package com.example.springbootwechatlogin.service;

import com.example.springbootwechatlogin.config.WechatProperties;
import com.example.springbootwechatlogin.model.WechatSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class WechatApiService {

    @Autowired
    private WechatProperties properties;

    private final RestTemplate restTemplate = new RestTemplate();

    public WechatSession miniProgramCode2Session(String code, String nickname) {
        if (properties.isMockEnabled()) {
            return mockSession("mini", code, nickname);
        }
        String url = "https://api.weixin.qq.com/sns/jscode2session?appid={appid}&secret={secret}&js_code={code}&grant_type=authorization_code";
        Map result = restTemplate.getForObject(
                url,
                Map.class,
                properties.getMiniProgram().getAppId(),
                properties.getMiniProgram().getSecret(),
                code);
        WechatSession session = new WechatSession();
        session.setOpenid(String.valueOf(result.get("openid")));
        session.setUnionid(result.get("unionid") == null ? null : String.valueOf(result.get("unionid")));
        session.setSessionKey(result.get("session_key") == null ? null : String.valueOf(result.get("session_key")));
        session.setNickname(nickname);
        return session;
    }

    public WechatSession officialAccountOauth(String code) {
        if (properties.isMockEnabled()) {
            return mockSession("mp", code, "公众号用户-" + code);
        }
        String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid={appid}&secret={secret}&code={code}&grant_type=authorization_code";
        Map result = restTemplate.getForObject(
                url,
                Map.class,
                properties.getOfficialAccount().getAppId(),
                properties.getOfficialAccount().getSecret(),
                code);
        WechatSession session = new WechatSession();
        session.setOpenid(String.valueOf(result.get("openid")));
        session.setUnionid(result.get("unionid") == null ? null : String.valueOf(result.get("unionid")));
        session.setAccessToken(result.get("access_token") == null ? null : String.valueOf(result.get("access_token")));
        session.setNickname("wechat-user-" + code);
        return session;
    }

    private WechatSession mockSession(String channel, String code, String nickname) {
        WechatSession session = new WechatSession();
        session.setOpenid(channel + "-openid-" + code);
        session.setUnionid("unionid-" + code);
        session.setSessionKey("mock-session-key-" + code);
        session.setAccessToken("mock-access-token-" + code);
        session.setNickname(nickname == null || nickname.trim().length() == 0 ? "微信用户-" + code : nickname);
        return session;
    }
}
