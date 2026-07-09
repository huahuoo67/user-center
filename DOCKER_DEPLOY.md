# Docker Deployment

本项目采用“两容器 + 独立 MySQL”的部署方式：

- `user-center-frontend`：Nginx 容器，托管前端 `dist`，并代理 `/api` 请求。
- `user-center-backend`：Spring Boot 容器，提供后端接口。
- MySQL：继续使用服务器已有数据库，不放进 Docker，避免迁移现有数据。

## 1. 构建产物

在项目根目录分别构建后端 jar 和前端 dist：

```bash
cd user-center-backend
mvn clean package -DskipTests

cd ../user-center-frontend-vue
npm install
npm run build
```

## 2. 数据库配置

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

## 3. 启动容器

回到项目根目录：

```bash
docker compose up -d --build
```

查看状态：

```bash
docker compose ps
```

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

代码更新后重复：

```bash
cd user-center-backend
mvn clean package -DskipTests

cd ../user-center-frontend-vue
npm run build

cd ..
docker compose up -d --build
```

如果需要清理旧容器：

```bash
docker compose down
docker compose up -d --build
```
