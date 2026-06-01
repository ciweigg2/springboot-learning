package com.example.springbootspringcloud.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "localEchoClient", url = "${demo.remote.base-url}")
public interface EchoClient {

    @GetMapping("/cloud/provider/echo")
    String echo(@RequestParam("message") String message);
}
