# Docker Deployment

本项目采用“两容器 + 独立 MySQL”的部署方式：

- `user-center-frontend`：Nginx 容器，托管前端 `dist`，并代理 `/api` 请求。
- `user-center-backend`：Spring Boot 容器，提供后端接口。
- MySQL：继续使用服务器已有数据库，不放进 Docker，避免迁移现有数据。

后端容器只在 Docker 网络内暴露 `8080`，不占用宿主机 `8080`，避免和宝塔 Java 项目冲突。

## 1. 构建方式

本项目使用 Docker 多阶段构建，服务器只需要安装 Docker 和 Docker Compose，不需要安装 Maven 或 Node.js。

后端镜像会先使用 Maven 镜像编译 jar，再复制到 Java 运行镜像中。

前端镜像会先使用 Node 镜像执行 `npm install` 和 `npm run build`，再复制 `dist` 到 Nginx 镜像中。

## 2. 数据库配置

首次部署先在宝塔数据库管理或 MySQL 命令行中执行：

```text
user-center-backend/sql/create_table.sql
```

脚本会创建 `user_center` 数据库和 `user` 表。数据库名必须与根目录 `.env`
中的 `MYSQL_URL` 保持一致。

Docker 后端使用：

```text
user-center-backend/src/main/resources/application-docker.yml
```

容器里不能用 `localhost` 访问宿主机 MySQL，因此配置使用：

```yaml
jdbc:mysql://host.docker.internal:3306/user_center
```

`docker-compose.yml` 中的：

```yaml
extra_hosts:
  - "host.docker.internal:host-gateway"
```

会让 Linux 服务器上的容器识别 `host.docker.internal`。

复制环境变量模板并填写服务器的真实配置：

```bash
cp .env.example .env
```

其中 `ADMIN_PASSWORD` 必须设置为至少 8 位的强密码。应用仅在首次找不到管理员账号时创建它：

```text
ADMIN_ACCOUNT=admin
ADMIN_PASSWORD=你设置的密码
```

后续重启或重新构建不会重置已有管理员的密码。如果不需要自动创建管理员，可设置
`ADMIN_ENABLED=false`。

## 3. 启动容器

回到项目根目录：

```bash
docker compose up -d --build
```

查看状态：

```bash
docker compose ps
```

正常情况下，`user-center-backend` 应显示为 `healthy`。如果后端反复重启或不健康，优先查看后端日志，常见原因是数据库未建表、MySQL 未允许 Docker 网段连接，或 `.env` 中数据库账号密码错误。

查看后端日志：

```bash
docker logs -f user-center-backend
```

查看前端日志：

```bash
docker logs -f user-center-frontend
```

## 4. 访问地址

当前 `docker-compose.yml` 为了避免和宝塔 Nginx 的 80 端口冲突，前端映射到：

```text
http://服务器IP:8081
```

确认 Docker 版运行正常后，如果要改成直接访问 80 端口，把：

```yaml
ports:
  - "8081:80"
```

改成：

```yaml
ports:
  - "80:80"
```

前提是服务器 80 端口没有被宝塔 Nginx 占用。

## 5. /api 代理说明

后端没有 `/api` 路由。真实接口是：

```text
/user/login
/user/register
/user/current
```

前端生产环境请求 `/api`，由前端容器内的 Nginx 转发：

```nginx
location /api/ {
    proxy_pass http://backend:8080/;
}
```

因此：

```text
/api/user/login -> backend:8080/user/login
```

## 6. 重新部署

代码更新后拉取最新代码，并重新构建镜像：

```bash
git pull
docker compose up -d --build
```

部署后建议依次验证：

1. 打开首页并注册一个不少于 4 位的账号，密码不少于 8 位。
2. 使用新账号登录，刷新页面后确认仍保持登录状态。
3. 使用 `.env` 中的 `ADMIN_ACCOUNT` 和首次部署时配置的 `ADMIN_PASSWORD` 登录。
4. 进入用户管理页，确认能看到用户列表。

如果需要清理旧容器：

```bash
docker compose down
docker compose up -d --build
```
