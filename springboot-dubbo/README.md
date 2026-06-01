# springboot-dubbo

Dubbo 使用教程模块，演示接口定义、服务暴露、服务引用和 HTTP 接口触发 Dubbo 调用。默认使用 Dubbo 本地 injvm 调用，不依赖外部注册中心，适合学习时开箱即用。

## 启动

```bash
mvn spring-boot:run -pl springboot-dubbo
```

默认端口：`8084`

## 接口示例

```bash
curl 'http://localhost:8084/dubbo/hello?name=ma'
```

## 常见写法

定义 Dubbo API：

```java
public interface GreetingService {

    String sayHello(String name);
}
```

暴露服务：

```java
@DubboService
public class GreetingServiceImpl implements GreetingService {
}
```

引用服务：

```java
@DubboReference(check = false, injvm = true)
private GreetingService greetingService;
```

接入注册中心时，把 `dubbo.registry.address=N/A` 改成 Nacos、ZooKeeper 等地址即可，例如：`nacos://127.0.0.1:8848`。
