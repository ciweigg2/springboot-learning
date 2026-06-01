package com.example.springbootspringcloud.controller;

import com.example.springbootspringcloud.client.EchoClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cloud")
public class CloudDemoController {

    @Value("${spring.application.name}")
    private String applicationName;

    @Autowired
    private EchoClient echoClient;

    @GetMapping("/hello")
    public String hello() {
        return "hello spring cloud, application=" + applicationName;
    }

    @GetMapping("/feign")
    public String feign(@RequestParam(defaultValue = "openfeign") String message) {
        return echoClient.echo(message);
    }

    @GetMapping("/provider/echo")
    public String echo(@RequestParam String message) {
        return "provider echo: " + message;
    }
}
