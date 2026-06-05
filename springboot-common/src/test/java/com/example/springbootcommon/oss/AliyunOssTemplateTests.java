package com.example.springbootcommon.oss;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class AliyunOssTemplateTests {

    @Test
    public void buildObjectUrlWithDomain() {
        AliyunOssProperties properties = new AliyunOssProperties();
        properties.setEndpoint("oss-cn-hangzhou.aliyuncs.com");
        properties.setBucketName("demo-bucket");
        properties.setDomain("https://static.example.com/");

        AliyunOssTemplate template = new AliyunOssTemplate(properties);

        assertEquals("https://static.example.com/images/a.png", template.buildObjectUrl("/images/a.png"));
    }

    @Test
    public void buildObjectUrlWithEndpoint() {
        AliyunOssProperties properties = new AliyunOssProperties();
        properties.setEndpoint("oss-cn-hangzhou.aliyuncs.com");
        properties.setBucketName("demo-bucket");

        AliyunOssTemplate template = new AliyunOssTemplate(properties);

        assertEquals("https://demo-bucket.oss-cn-hangzhou.aliyuncs.com/images/a.png", template.buildObjectUrl("images/a.png"));
    }
}
