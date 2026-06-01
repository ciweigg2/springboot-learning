package com.example.springbootmybatisplus.dto;

import lombok.Data;

@Data
public class UserUpdateRequest {

    private String username;

    private Integer age;

    private String email;
}
