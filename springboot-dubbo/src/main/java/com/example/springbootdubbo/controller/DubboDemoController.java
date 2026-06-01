package com.example.springbootdubbo.controller;

import com.example.springbootdubbo.api.GreetingService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dubbo")
public class DubboDemoController {

    @DubboReference(check = false, injvm = true)
    private GreetingService greetingService;

    @GetMapping("/hello")
    public String hello(@RequestParam(defaultValue = "learning") String name) {
        return greetingService.sayHello(name);
    }
}
