package com.example.springbootcommon.oss;

import lombok.Data;

@Data
public class AliyunOssProperties {

    private String endpoint;

    private String accessKeyId;

    private String accessKeySecret;

    private String bucketName;

    private String domain;
}
