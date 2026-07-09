# 用户中心系统

一个面向 Java 实习展示的前后端分离用户中心项目，覆盖用户注册、登录、注销、当前用户获取、管理员用户查询与删除等基础能力。项目重点体现接口规范、登录态维护、权限校验、前后端联调和基础安全实践。

## 技术栈

- 后端：Java 21、Spring Boot 3、MyBatis-Plus、MySQL、JUnit 5
- 前端：Vue 3、TypeScript、Vue Router、Pinia、Axios、Ant Design Vue、Vite
- 安全：Session 登录态、管理员角色校验、BCrypt 密码哈希

## 项目结构

```text
user-center-backend/        Spring Boot 后端服务
  src/main/java/com/fzq     Controller、Service、Mapper、异常与通用返回
  src/main/resources        应用配置与 MyBatis XML
  src/test/java             后端单元测试
  sql/create_table.sql      数据库建表脚本

user-center-frontend-vue/   Vue 前端应用
  src/api                   接口请求封装
  src/pages                 登录、注册、用户管理页面
  src/router                路由配置
  src/store                 Pinia 登录用户状态
  src/types                 前端 TypeScript 类型
```

## 核心功能

- 用户注册：校验账号、密码和确认密码，使用 BCrypt 存储密码哈希。
- 用户登录：校验账号密码，登录成功后写入 Session。
- 当前用户：通过 Session 获取登录用户，并返回脱敏后的用户信息。
- 权限控制：普通用户只能访问基础接口，管理员可查询和删除用户。
- 前端登录态：Axios 拦截器统一处理未登录跳转，Pinia 保存当前用户。

## 本地启动

1. 创建数据库并执行 `user-center-backend/sql/create_table.sql`。
2. 修改 `user-center-backend/src/main/resources/application-dev.yml` 中的本地数据库连接。
3. 启动后端：

```bash
cd user-center-backend
mvn spring-boot:run
```

4. 启动前端：

```bash
cd user-center-frontend-vue
npm install
npm run dev
```

## 常用命令

```bash
# 后端测试与打包
cd user-center-backend
mvn test
mvn package

# 前端检查与构建
cd user-center-frontend-vue
npm run lint
npm run build
```

## 简历描述参考

- 基于 Spring Boot 3 和 MyBatis-Plus 实现用户注册、登录、注销、查询、删除等接口。
- 使用统一响应结构和全局异常处理规范接口错误返回。
- 使用 Session 维护登录态，并基于用户角色实现管理员权限校验。
- 使用 BCrypt 对密码进行单向哈希存储，避免明文密码和弱哈希风险。
- 前端使用 Vue 3、Pinia、Axios 拦截器实现登录状态管理和未登录自动跳转。
