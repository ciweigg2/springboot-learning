# springboot-springcloud

Spring Cloud 使用教程模块，基于当前项目的 Spring Boot 2.1.x 选择 `Greenwich.SR6`，示例重点放在 OpenFeign 声明式 HTTP 调用。

## 启动

```bash
mvn spring-boot:run -pl springboot-springcloud
```

默认端口：`8083`

## 接口示例

基础接口：

```bash
curl http://localhost:8083/cloud/hello
```

OpenFeign 调用本应用内的 Provider 接口：

```bash
curl 'http://localhost:8083/cloud/feign?message=hello'
```

Provider 接口：

```bash
curl 'http://localhost:8083/cloud/provider/echo?message=hello'
```

## 常见写法

启用 Feign：

```java
@EnableFeignClients
@SpringBootApplication
public class SpringbootSpringcloudApplication {
}
```

声明 Feign Client：

```java
@FeignClient(name = "localEchoClient", url = "${demo.remote.base-url}")
public interface EchoClient {

    @GetMapping("/cloud/provider/echo")
    String echo(@RequestParam("message") String message);
}
```

实际项目中通常会把 `url` 换成服务名，并结合 Nacos、Eureka、Consul 等注册中心做服务发现。
