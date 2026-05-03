# ===========================================
# 阶段一：构建阶段 (Build Stage)
# 使用 Maven 官方镜像进行项目编译和打包
# ===========================================
FROM maven:3.9.9-eclipse-temurin-21-alpine AS builder

# 设置工作目录
WORKDIR /build

# 首先复制 Maven 配置文件，利用 Docker 层缓存机制
# 如果 pom.xml 没有变化，后续构建可以复用已下载的依赖
COPY pom.xml .
COPY .mvn/ .mvn/
COPY mvnw mvnw.cmd ./

# 下载项目依赖（离线模式，加快后续构建）
RUN mvn dependency:go-offline -B

# 复制源代码
COPY src/ src/

# 执行 Maven 打包，跳过测试以加快构建速度
# 使用 -DskipTests 跳过测试，-B 批处理模式
RUN mvn clean package -DskipTests -B

# ===========================================
# 阶段二：运行阶段 (Runtime Stage)
# 使用轻量级的 JRE 运行时镜像
# ===========================================
FROM eclipse-temurin:21-jre-alpine

# 维护者信息
LABEL maintainer="jiangye"
LABEL description="Jiang AI Agent - Spring Boot 3 + Java 21 + Spring AI"

# 设置时区为上海
ENV TZ=Asia/Shanghai

# 安装必要的工具（时区数据、字体等）
RUN apk add --no-cache tzdata \
    && cp /usr/share/zoneinfo/${TZ} /etc/localtime \
    && echo ${TZ} > /etc/timezone \
    && apk del tzdata

# 创建应用运行用户（安全最佳实践：不使用 root 运行应用）
RUN addgroup -S appgroup && adduser -S appuser -G appgroup

# 设置工作目录
WORKDIR /app

# 从构建阶段复制打包好的 JAR 文件
# 使用 --from=builder 从第一阶段复制文件
COPY --from=builder /build/target/*.jar app.jar

# 创建日志目录并设置权限
RUN mkdir -p /app/logs && chown -R appuser:appgroup /app

# 切换到非 root 用户运行
USER appuser

# 暴露应用端口（与 application.yml 中配置一致）
EXPOSE 8123

# 健康检查：每 30 秒检查一次，超时 3 秒，连续 3 次失败视为不健康
HEALTHCHECK --interval=30s --timeout=3s --start-period=60s --retries=3 \
    CMD wget --no-verbose --tries=1 --spider http://localhost:8123/api/actuator/health || exit 1

# JVM 参数优化（可根据实际情况调整）
# -XX:+UseContainerSupport: 启用容器支持，自动识别容器内存限制
# -XX:MaxRAMPercentage=75.0: 使用容器内存的 75%
# -XX:+UseG1GC: 使用 G1 垃圾收集器
# -XX:+UseStringDeduplication: 启用字符串去重
# -Djava.security.egd=file:/dev/./urandom: 加速随机数生成
ENV JAVA_OPTS="-XX:+UseContainerSupport \
    -XX:MaxRAMPercentage=75.0 \
    -XX:InitialRAMPercentage=50.0 \
    -XX:+UseG1GC \
    -XX:+UseStringDeduplication \
    -XX:+OptimizeStringConcat \
    -Djava.security.egd=file:/dev/./urandom \
    -Dfile.encoding=UTF-8 \
    -Dspring.backgroundpreinitializer.ignore=true"

# 启动命令
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
