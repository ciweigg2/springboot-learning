package com.example.springbootcommon.oss;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AliyunOssUploadResult {

    private String bucketName;

    private String objectName;

    private String url;

    private String eTag;
}
