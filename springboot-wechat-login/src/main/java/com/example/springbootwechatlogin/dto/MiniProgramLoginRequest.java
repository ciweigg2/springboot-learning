package com.example.springbootwechatlogin.dto;

import lombok.Data;

@Data
public class MiniProgramLoginRequest {

    private String code;

    private String nickname;
}
