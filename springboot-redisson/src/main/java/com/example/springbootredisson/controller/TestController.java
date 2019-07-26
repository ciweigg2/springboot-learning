package com.example.springbootredisson.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 马秀成
 * @date 2019/7/26
 * @jdk.version 1.8
 * @desc
 */
@RestController
public class TestController {

    @Autowired
    private RedisTemplate redisTemplate;

    @RequestMapping(value = "test")
    public void test() {
        ValueOperations valueOperations = redisTemplate.opsForValue();
        valueOperations.set("redisson", "hello word");
    }

}
