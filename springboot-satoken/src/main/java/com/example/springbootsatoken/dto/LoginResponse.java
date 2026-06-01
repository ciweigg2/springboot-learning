package com.example.springbootsatoken.dto;

import lombok.Data;

import java.util.List;

@Data
public class LoginResponse {

    private Long userId;

    private String username;

    private String nickname;

    private String tokenName;

    private String tokenValue;

    private List<String> roles;

    private List<String> permissions;
}
