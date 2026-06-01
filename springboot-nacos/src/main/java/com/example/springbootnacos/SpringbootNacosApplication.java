package com.example.springbootnacos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class SpringbootNacosApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringbootNacosApplication.class, args);
    }
}
