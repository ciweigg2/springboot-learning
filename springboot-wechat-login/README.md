# springboot-wechat-login

微信登录教程模块，默认使用 H2 内存数据库和 mock 微信接口，启动即可跑小程序登录、公众号 OAuth 回调、绑定手机号、查询当前用户等流程。

## 启动

```bash
mvn spring-boot:run -pl springboot-wechat-login
```

默认端口：`8087`

H2 控制台：`http://localhost:8087/h2-console`

- JDBC URL：`jdbc:h2:mem:wechat_login_demo`
- 用户名：`sa`
- 密码：留空

## 表结构

| 表名 | 说明 |
| --- | --- |
| app_user | 系统本地用户表 |
| wechat_user | 微信身份表，保存 openid、unionid、session_key、access_token |
| login_session | 本地登录态表 |

## Demo 1：小程序 code 登录

```bash
curl -X POST http://localhost:8087/wechat/mini/login \
  -H 'Content-Type: application/json' \
  -d '{"code":"demo-code","nickname":"小程序用户"}'
```

流程：

1. 前端调用 `wx.login()` 获取 `code`
2. 后端用 `code` 换取 `openid/session_key`
3. 根据 `login_type + openid` 查找或创建本地用户
4. 生成本地 `token`

## Demo 2：公众号 OAuth 回调

先获取 mock 回调地址：

```bash
curl http://localhost:8087/wechat/official/auth-url
```

再访问返回的 `mockCallback`：

```bash
curl 'http://localhost:8087/wechat/official/callback?code=mock-code-001&state=demo-state'
```

## Demo 3：绑定手机号

把登录返回的 `token` 填进去：

```bash
curl -X POST http://localhost:8087/wechat/bind-phone \
  -H 'Content-Type: application/json' \
  -d '{"token":"替换成登录返回的token","phone":"13800000002"}'
```

## Demo 4：查询当前用户

```bash
curl http://localhost:8087/wechat/me -H 'token: 替换成登录返回的token'
```

## 接入真实微信

默认配置是 mock：

```yaml
wechat:
  mock-enabled: true
```

接入真实微信时改成：

```yaml
wechat:
  mock-enabled: false
  mini-program:
    app-id: 你的小程序 appId
    secret: 你的小程序 secret
  official-account:
    app-id: 你的公众号 appId
    secret: 你的公众号 secret
    redirect-uri: https://你的域名/wechat/official/callback
```

真实接口示例：

- 小程序：`jscode2session`
- 公众号：`oauth2/access_token`

本模块为了学习流程，没有接入微信手机号解密和 JWT；生产项目通常还会增加签名校验、手机号解密、Refresh Token、风控、登录日志和账号合并策略。
