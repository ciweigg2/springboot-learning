# springboot-common

公共方法模块，目前包含阿里云 OSS 通用工具类。

## OSS 配置示例

```java
AliyunOssProperties properties = new AliyunOssProperties();
properties.setEndpoint("oss-cn-hangzhou.aliyuncs.com");
properties.setAccessKeyId("你的 AccessKeyId");
properties.setAccessKeySecret("你的 AccessKeySecret");
properties.setBucketName("你的 bucket");
properties.setDomain("https://static.example.com");

AliyunOssTemplate ossTemplate = new AliyunOssTemplate(properties);
```

## 常用方法

上传字节数组：

```java
AliyunOssUploadResult result = ossTemplate.uploadBytes(
        "images/demo.png",
        bytes,
        "image/png");
```

上传文件：

```java
AliyunOssUploadResult result = ossTemplate.uploadFile(
        "files/demo.pdf",
        new File("/tmp/demo.pdf"),
        "application/pdf");
```

下载文件：

```java
try (InputStream inputStream = ossTemplate.download("files/demo.pdf")) {
    // 读取 inputStream
}
```

判断文件是否存在：

```java
boolean exists = ossTemplate.exists("files/demo.pdf");
```

删除文件：

```java
ossTemplate.delete("files/demo.pdf");
```

生成临时访问链接：

```java
URL url = ossTemplate.generatePresignedUrl("private/demo.pdf", 3600);
```

## 说明

- `domain` 不为空时，上传结果 URL 使用自定义域名。
- `domain` 为空时，上传结果 URL 使用 `https://{bucket}.{endpoint}/{objectName}`。
- 下载方法返回的 `InputStream` 关闭时会自动关闭 OSS 客户端。
- 生产环境不要把 AccessKey 写死在代码里，建议使用环境变量、配置中心或 RAM 角色。
