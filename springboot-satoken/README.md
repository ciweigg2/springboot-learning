# springboot-satoken

Sa-Token 登录和权限教程模块，默认使用 H2 内存数据库初始化一套 RBAC 表结构，启动即可演示登录、Token 信息、登录拦截、权限校验、角色校验、手动校验、踢人下线和注销。

## 启动

```bash
mvn spring-boot:run -pl springboot-satoken
```

默认端口：`8086`

H2 控制台：`http://localhost:8086/h2-console`

- JDBC URL：`jdbc:h2:mem:satoken_demo`
- 用户名：`sa`
- 密码：留空

## 表结构

本模块启动时会自动执行：

- `src/main/resources/schema.sql`：用户、角色、权限、用户角色、角色权限、登录日志表
- `src/main/resources/data.sql`：初始化演示账号、角色、权限关系

核心表：

| 表名 | 说明 |
| --- | --- |
| sys_user | 用户表，包含账号、昵称、密码哈希、盐、状态、最后登录时间 |
| sys_role | 角色表 |
| sys_permission | 权限表 |
| sys_user_role | 用户角色关联表 |
| sys_role_permission | 角色权限关联表 |
| sys_login_log | 登录日志表，记录成功/失败原因 |

## 示例账号

| 用户名 | 密码 | 角色 | 权限 |
| --- | --- | --- | --- |
| admin | 123456 | admin,user | user:list,user:add,user:update,user:delete,order:list,security:kickout |
| user | 123456 | user | user:list,order:list |
| disabled | 123456 | user | 账号已停用，无法登录 |

## 登录

```bash
curl -X POST http://localhost:8086/auth/login \
  -H 'Content-Type: application/json' \
  -d '{"username":"admin","password":"123456"}'
```

返回里的 `tokenName` 和 `tokenValue` 后续请求要放到 Header：

```bash
curl http://localhost:8086/permission/profile \
  -H 'satoken: 替换成登录返回的 tokenValue'
```

## 常用接口

公开接口，不需要登录：

```bash
curl http://localhost:8086/public/hello
```

查看当前登录信息、角色、权限：

```bash
curl http://localhost:8086/permission/profile -H 'satoken: tokenValue'
```

权限校验：

```bash
curl http://localhost:8086/permission/users -H 'satoken: tokenValue'
```

角色校验：

```bash
curl http://localhost:8086/permission/admin -H 'satoken: tokenValue'
```

手动校验：

```bash
curl http://localhost:8086/permission/manual-check -H 'satoken: tokenValue'
```

踢人下线，仅 admin 角色可用：

```bash
curl -X POST http://localhost:8086/permission/kickout/2 -H 'satoken: tokenValue'
```

注销：

```bash
curl -X POST http://localhost:8086/auth/logout -H 'satoken: tokenValue'
```

## 常见写法

真实登录流程：

1. 根据用户名查询 `sys_user`
2. 判断用户是否存在、是否停用、是否删除
3. 使用 `SHA-256(salt + password)` 校验密码
4. 更新最后登录时间
5. 写入 `sys_login_log`
6. 调用 `StpUtil.login(userId)` 生成登录态

登录成功后写入登录态并返回 Token：

```java
AuthUser user = demoUserService.login(request.getUsername(), request.getPassword(), getClientIp(servletRequest));
StpUtil.login(user.getId());
SaTokenInfo tokenInfo = StpUtil.getTokenInfo();
```

配置登录拦截：

```java
registry.addInterceptor(new SaInterceptor(handle -> StpUtil.checkLogin()))
        .addPathPatterns("/**")
        .excludePathPatterns("/auth/login", "/public/**");
```

实现角色和权限加载：

```java
@Component
public class StpInterfaceImpl implements StpInterface {

    public List<String> getPermissionList(Object loginId, String loginType) {
        return demoUserService.getPermissionList(String.valueOf(loginId));
    }

    public List<String> getRoleList(Object loginId, String loginType) {
        return demoUserService.getRoleList(String.valueOf(loginId));
    }
}
```

权限从 RBAC 关联表查询：

```sql
SELECT DISTINCT p.permission_code
FROM sys_permission p
JOIN sys_role_permission rp ON rp.permission_id = p.id
JOIN sys_user_role ur ON ur.role_id = rp.role_id
JOIN sys_role r ON r.id = ur.role_id
WHERE ur.user_id = ?
  AND p.status = 1
  AND r.status = 1
ORDER BY p.permission_code
```

注解校验：

```java
@SaCheckPermission("user:list")
public String listUsers() {
    return "user:list permission pass";
}

@SaCheckRole("admin")
public String adminOnly() {
    return "admin role pass";
}

@SaCheckRole("admin")
@SaCheckPermission("security:kickout")
public String kickout(String userId) {
    StpUtil.kickout(userId);
    return "kickout userId=" + userId;
}
```

手动校验：

```java
StpUtil.checkLogin();
StpUtil.checkPermission("order:list");
StpUtil.checkRole("admin");
```
