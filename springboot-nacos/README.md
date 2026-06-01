# springboot-nacos

Nacos 使用教程模块，演示服务注册发现、配置读取和服务实例查询。Nacos 是注册中心/配置中心，启动本模块前需要先启动 Nacos Server。

## 启动 Nacos

本地默认地址：`127.0.0.1:8848`

如果已经有 Nacos，直接修改 `src/main/resources/bootstrap.yml` 中的 `spring.cloud.nacos.server-addr` 即可。

## 启动模块

```bash
mvn spring-boot:run -pl springboot-nacos
```

默认端口：`8085`

## 接口示例

读取配置：

```bash
curl http://localhost:8085/nacos/config
```

查询服务列表：

```bash
curl http://localhost:8085/nacos/services
```

查询某个服务实例：

```bash
curl http://localhost:8085/nacos/instances/springboot-nacos
```

## Nacos 配置示例

在 Nacos 配置中心新增配置：

- Data ID：`springboot-nacos.yaml`
- Group：`DEFAULT_GROUP`
- 配置格式：`YAML`

```yaml
demo:
  message: hello from nacos config center
```

代码中读取配置：

```java
@Value("${demo.message:hello nacos}")
private String message;
```

服务发现常见写法：

```java
discoveryClient.getServices();
discoveryClient.getInstances("springboot-nacos");
```
