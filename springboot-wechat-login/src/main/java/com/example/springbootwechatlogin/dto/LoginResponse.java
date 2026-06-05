package com.example.springbootwechatlogin.dto;

import lombok.Data;

@Data
public class LoginResponse {

    private Long userId;

    private Long wechatUserId;

    private String openid;

    private String unionid;

    private String nickname;

    private String phone;

    private String token;

    private boolean newUser;
}
