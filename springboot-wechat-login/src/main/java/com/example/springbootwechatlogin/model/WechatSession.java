package com.example.springbootwechatlogin.model;

import lombok.Data;

@Data
public class WechatSession {

    private String openid;

    private String unionid;

    private String sessionKey;

    private String accessToken;

    private String nickname;
}
