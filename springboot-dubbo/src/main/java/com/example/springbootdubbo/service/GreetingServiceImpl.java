package com.example.springbootdubbo.service;

import com.example.springbootdubbo.api.GreetingService;
import org.apache.dubbo.config.annotation.DubboService;

@DubboService
public class GreetingServiceImpl implements GreetingService {

    @Override
    public String sayHello(String name) {
        return "hello dubbo, " + name;
    }
}
