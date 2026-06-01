# springboot-mybatis-plus

MyBatis-Plus 使用教程模块，默认连接 H2 内存数据库，启动后自动建表并插入示例数据，适合学习时开箱即用。

## 启动

```bash
mvn spring-boot:run -pl springboot-mybatis-plus
```

默认端口：`8082`

H2 控制台：`http://localhost:8082/h2-console`

- JDBC URL：`jdbc:h2:mem:mybatis_plus_demo`
- 用户名：`sa`
- 密码：留空

## 连接 MySQL

默认配置使用 H2，想连接 MySQL 时先建库：

```sql
CREATE DATABASE springboot_learning DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

然后修改 `src/main/resources/application.yml` 中 `mysql` profile 的账号密码，使用下面命令启动：

```bash
mvn spring-boot:run -pl springboot-mybatis-plus -Dspring-boot.run.profiles=mysql
```

## 增删改查接口

新增用户：

```bash
curl -X POST http://localhost:8082/users \
  -H 'Content-Type: application/json' \
  -d '{"username":"zhaoliu","age":20,"email":"zhaoliu@example.com"}'
```

查询详情：

```bash
curl http://localhost:8082/users/1
```

分页和关键字查询：

```bash
curl 'http://localhost:8082/users?pageNum=1&pageSize=10&keyword=li'
```

修改用户：

```bash
curl -X PUT http://localhost:8082/users/1 \
  -H 'Content-Type: application/json' \
  -d '{"username":"zhangsan-new","age":19,"email":"new@example.com"}'
```

删除用户：

```bash
curl -X DELETE http://localhost:8082/users/1
```

## 常见写法

继承 `BaseMapper<T>` 后，Mapper 自动拥有基础 CRUD：

```java
public interface UserMapper extends BaseMapper<User> {
}
```

继承 `ServiceImpl<Mapper, Entity>` 后，可以直接使用 `save`、`getById`、`updateById`、`removeById`、`page`、`list` 等方法：

```java
@Service
public class UserService extends ServiceImpl<UserMapper, User> {
}
```

Lambda 条件查询适合日常业务，字段重构时更安全：

```java
list(new LambdaQueryWrapper<User>()
        .ge(User::getAge, 18)
        .orderByAsc(User::getAge));
```

普通 `QueryWrapper` 适合演示指定查询字段、动态 SQL 片段等写法：

```java
userMapper.selectList(new QueryWrapper<User>()
        .select("id", "username", "age", "email")
        .ge("age", 18)
        .orderByDesc("id"));
```

分页需要配置 `MybatisPlusInterceptor` 和 `PaginationInnerInterceptor`，本模块已在 `MybatisPlusConfig` 中配置好。
