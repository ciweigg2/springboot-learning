package com.example.springbootwechatlogin.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "wechat")
public class WechatProperties {

    private boolean mockEnabled = true;

    private MiniProgram miniProgram = new MiniProgram();

    private OfficialAccount officialAccount = new OfficialAccount();

    @Data
    public static class MiniProgram {

        private String appId = "demo-mini-appid";

        private String secret = "demo-mini-secret";
    }

    @Data
    public static class OfficialAccount {

        private String appId = "demo-mp-appid";

        private String secret = "demo-mp-secret";

        private String redirectUri = "http://127.0.0.1:8087/wechat/official/callback";
    }
}
