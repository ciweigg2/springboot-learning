package com.example.springbootsatoken.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AuthUser {

    private Long id;

    private String username;

    private String nickname;

    private String passwordHash;

    private String passwordSalt;

    private Integer status;

    private LocalDateTime lastLoginTime;
}
