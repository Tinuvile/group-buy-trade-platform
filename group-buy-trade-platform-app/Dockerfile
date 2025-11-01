# 基础镜像
FROM openjdk:8-jre-slim

# 元数据
LABEL maintainer="xiaofuge"
LABEL description="Group Buy Trade Platform Application"
LABEL version="1.0.0"

# 创建非root用户
RUN groupadd -r appuser && useradd -r -g appuser appuser

# 创建应用目录
RUN mkdir -p /app && chown -R appuser:appuser /app
WORKDIR /app

# 配置环境变量
ENV PARAMS=""
ENV JAVA_OPTS="-Xmx512m -Xms256m"

# 安装必要工具并配置时区
ENV TZ=PRC
RUN apt-get update && apt-get install -y --no-install-recommends curl \
    && ln -snf /usr/share/zoneinfo/$TZ /etc/localtime \
    && echo $TZ > /etc/timezone \
    && rm -rf /var/lib/apt/lists/*

# 添加应用 (使用COPY而不是ADD)
COPY --chown=appuser:appuser target/group-buy-trade-platform-app.jar /app/group-buy-trade-platform-app.jar

# 切换到非root用户
USER appuser

# 暴露端口
EXPOSE 8091

# 健康检查 - 检查端口是否可访问
HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
    CMD curl -f http://localhost:8091/ || curl -f http://localhost:8091/health || exit 1

# 启动应用
ENTRYPOINT ["sh","-c","java $JAVA_OPTS -jar /app/group-buy-trade-platform-app.jar $PARAMS"]