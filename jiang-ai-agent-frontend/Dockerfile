# ===========================================
# 阶段一：构建阶段 (Build Stage)
# 使用 Node.js 官方镜像进行前端项目构建
# ===========================================
FROM node:20-alpine AS build

# 设置工作目录
WORKDIR /app

# 设置 Node 环境变量
ENV NODE_ENV=production

# 首先复制 package.json 和 package-lock.json，利用 Docker 层缓存
COPY package*.json ./

# 安装项目依赖
# --frozen-lockfile 确保使用 package-lock.json 中锁定的版本
RUN npm ci --frozen-lockfile

# 复制项目源代码
COPY . .

# 执行构建命令
RUN npm run build

# ===========================================
# 阶段二：运行阶段 (Runtime Stage)
# 使用轻量级的 Nginx Alpine 镜像
# ===========================================
FROM nginx:alpine

# 维护者信息
LABEL maintainer="jiangye"
LABEL description="Jiang AI Agent Frontend - Vue 3 + Vite"

# 设置时区为上海
ENV TZ=Asia/Shanghai

# 安装 tzdata 并设置时区
RUN apk add --no-cache tzdata \
    && cp /usr/share/zoneinfo/${TZ} /etc/localtime \
    && echo ${TZ} > /etc/timezone \
    && apk del tzdata

# 从构建阶段复制打包好的静态文件到 Nginx 默认目录
COPY --from=build /app/dist /usr/share/nginx/html

# 复制自定义 Nginx 配置文件
COPY nginx.conf /etc/nginx/conf.d/default.conf

# 创建必要的目录并设置权限
RUN mkdir -p /var/log/nginx /var/cache/nginx \
    && chown -R nginx:nginx /var/log/nginx /var/cache/nginx /usr/share/nginx/html

# 健康检查：每 30 秒检查一次 Nginx 是否正常运行
HEALTHCHECK --interval=30s --timeout=3s --start-period=10s --retries=3 \
    CMD wget --no-verbose --tries=1 --spider http://localhost/ || exit 1

# 暴露 80 端口
EXPOSE 80

# 启动 Nginx（前台运行）
CMD ["nginx", "-g", "daemon off;"]
