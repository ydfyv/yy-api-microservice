# Docker 镜像构建
# @author 阿狸

FROM maven:3.8.6-openjdk-8 AS builder
WORKDIR /build
COPY . .
RUN mvn clean package -DskipTests

FROM eclipse-temurin:8-jre-alpine
WORKDIR /app
COPY --from=builder /build/target/yy-api-backend.jar app.jar
EXPOSE 8111 20882
ENTRYPOINT ["java", "-jar", "app.jar", "--spring.profiles.active=prod"]