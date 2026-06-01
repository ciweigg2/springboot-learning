package com.example.springbootmybatisplus.dto;

import lombok.Data;

@Data
public class UserCreateRequest {

    private String username;

    private Integer age;

    private String email;
}
