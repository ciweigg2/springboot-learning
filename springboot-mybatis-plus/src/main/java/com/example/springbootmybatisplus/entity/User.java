package com.example.springbootmybatisplus.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("demo_user")
public class User {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String username;

    private Integer age;

    private String email;

    private LocalDateTime createTime;
}
