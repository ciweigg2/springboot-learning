package com.example.springbootnacos.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/nacos")
public class NacosDemoController {

    @Value("${demo.message:hello nacos}")
    private String message;

    @Autowired
    private DiscoveryClient discoveryClient;

    @GetMapping("/config")
    public String config() {
        return message;
    }

    @GetMapping("/services")
    public List<String> services() {
        return discoveryClient.getServices();
    }

    @GetMapping("/instances/{serviceId}")
    public List<ServiceInstance> instances(@PathVariable String serviceId) {
        return discoveryClient.getInstances(serviceId);
    }
}
