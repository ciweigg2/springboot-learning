package com.example.springbootcommon.oss;

import com.aliyun.oss.OSS;
import com.aliyun.oss.model.GetObjectRequest;
import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectResult;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;

public class AliyunOssTemplate {

    private final AliyunOssProperties properties;

    private final AliyunOssClientFactory clientFactory;

    public AliyunOssTemplate(AliyunOssProperties properties) {
        this.properties = properties;
        this.clientFactory = new AliyunOssClientFactory(properties);
    }

    public AliyunOssUploadResult uploadBytes(String objectName, byte[] content, String contentType) {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(content.length);
        if (contentType != null && contentType.length() > 0) {
            metadata.setContentType(contentType);
        }
        return upload(objectName, new ByteArrayInputStream(content), metadata);
    }

    public AliyunOssUploadResult uploadFile(String objectName, File file, String contentType) {
        ObjectMetadata metadata = new ObjectMetadata();
        if (contentType != null && contentType.length() > 0) {
            metadata.setContentType(contentType);
        }
        OSS client = clientFactory.createClient();
        try {
            PutObjectResult result = client.putObject(properties.getBucketName(), objectName, file, metadata);
            return new AliyunOssUploadResult(properties.getBucketName(), objectName, buildObjectUrl(objectName), result.getETag());
        } finally {
            client.shutdown();
        }
    }

    public AliyunOssUploadResult upload(String objectName, InputStream inputStream, ObjectMetadata metadata) {
        OSS client = clientFactory.createClient();
        try {
            PutObjectResult result = client.putObject(properties.getBucketName(), objectName, inputStream, metadata);
            return new AliyunOssUploadResult(properties.getBucketName(), objectName, buildObjectUrl(objectName), result.getETag());
        } finally {
            client.shutdown();
        }
    }

    public InputStream download(String objectName) {
        OSS client = clientFactory.createClient();
        OSSObject object = client.getObject(new GetObjectRequest(properties.getBucketName(), objectName));
        return new OssInputStream(object.getObjectContent(), client);
    }

    public boolean exists(String objectName) {
        OSS client = clientFactory.createClient();
        try {
            return client.doesObjectExist(properties.getBucketName(), objectName);
        } finally {
            client.shutdown();
        }
    }

    public void delete(String objectName) {
        OSS client = clientFactory.createClient();
        try {
            client.deleteObject(properties.getBucketName(), objectName);
        } finally {
            client.shutdown();
        }
    }

    public URL generatePresignedUrl(String objectName, long expireSeconds) {
        OSS client = clientFactory.createClient();
        try {
            Date expiration = new Date(System.currentTimeMillis() + expireSeconds * 1000);
            return client.generatePresignedUrl(properties.getBucketName(), objectName, expiration);
        } finally {
            client.shutdown();
        }
    }

    public String buildObjectUrl(String objectName) {
        if (properties.getDomain() != null && properties.getDomain().length() > 0) {
            return trimEnd(properties.getDomain(), "/") + "/" + trimStart(objectName, "/");
        }
        String endpoint = properties.getEndpoint();
        String protocol = endpoint.startsWith("http://") || endpoint.startsWith("https://") ? "" : "https://";
        return protocol + properties.getBucketName() + "." + endpoint.replace("https://", "").replace("http://", "") + "/" + trimStart(objectName, "/");
    }

    private String trimStart(String value, String prefix) {
        while (value.startsWith(prefix)) {
            value = value.substring(prefix.length());
        }
        return value;
    }

    private String trimEnd(String value, String suffix) {
        while (value.endsWith(suffix)) {
            value = value.substring(0, value.length() - suffix.length());
        }
        return value;
    }
}
