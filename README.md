### SpringBoot学习demo

这是一个用于学习 Spring Boot 常见能力的开箱即用项目，每个模块都尽量提供可运行示例和简短教程。

* springboot-async (springBoot异步任务、异步监控)
* springboot-common (公共方法模块，包含阿里云 OSS 上传、下载、删除、签名 URL 等通用工具)
* springboot-redisson (Spring Boot 集成 Redisson，支持单机、集群、哨兵配置，并使用 RedisTemplate 操作)
* springboot-mybatis-plus (MyBatis-Plus 使用教程，包含数据库连接、增删改查、分页查询、Wrapper 等常见写法)
* springboot-springcloud (Spring Cloud OpenFeign 使用教程，演示声明式 HTTP 调用)
* springboot-dubbo (Dubbo 使用教程，演示接口定义、服务暴露、服务引用和本地调用)
* springboot-nacos (Nacos 使用教程，演示服务注册发现、配置读取和实例查询)
* springboot-satoken (Sa-Token 登录权限教程，演示登录、Token、角色、权限、注销、踢人下线等常用写法)
* springboot-wechat-login (微信登录教程，演示小程序 code 登录、公众号 OAuth 回调、绑定手机号和本地登录态)

#### 模块说明

| 模块 | 说明 | 默认端口 |
| --- | --- | --- |
| springboot-common | 公共方法模块，包含阿里云 OSS 通用工具 | - |
| springboot-async | 异步任务提交、异步任务状态监控 | 8080 |
| springboot-redisson | Redisson 连接 Redis 的单机、集群、哨兵配置示例 | 8080 |
| springboot-mybatis-plus | MyBatis-Plus CRUD 和数据库连接学习示例，默认使用 H2 内存库开箱即用 | 8082 |
| springboot-springcloud | Spring Cloud OpenFeign 声明式调用示例 | 8083 |
| springboot-dubbo | Dubbo 服务暴露与引用示例，默认本地 injvm 调用 | 8084 |
| springboot-nacos | Nacos 注册发现与配置中心示例，需要本地或远程 Nacos Server | 8085 |
| springboot-satoken | Sa-Token 登录认证、权限校验、角色校验和 Token 管理示例 | 8086 |
| springboot-wechat-login | 微信登录示例，默认 mock 微信接口并使用 H2 保存本地用户和登录态 | 8087 |
