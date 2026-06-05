package com.example.springbootwechatlogin.dto;

import lombok.Data;

@Data
public class BindPhoneRequest {

    private String token;

    private String phone;
}
