package com.example.springbootcommon.oss;

import com.aliyun.oss.ClientBuilderConfiguration;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;

public class AliyunOssClientFactory {

    private final AliyunOssProperties properties;

    public AliyunOssClientFactory(AliyunOssProperties properties) {
        this.properties = properties;
    }

    public OSS createClient() {
        ClientBuilderConfiguration configuration = new ClientBuilderConfiguration();
        configuration.setSupportCname(properties.getDomain() != null && properties.getDomain().length() > 0);
        return new OSSClientBuilder().build(
                properties.getEndpoint(),
                properties.getAccessKeyId(),
                properties.getAccessKeySecret(),
                configuration);
    }
}
