# 首次构建并启动（加 --build 强制重新构建镜像）
docker-compose up -d --build

# 查看日志
docker-compose logs -f yy-api-backend
docker-compose logs -f yy-api-gateway

# 停止
docker-compose down