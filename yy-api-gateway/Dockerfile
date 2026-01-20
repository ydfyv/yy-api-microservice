# author: 阿狸

# 多阶段构建：第一阶段编译
FROM maven:3.8.6-openjdk-8 AS builder
WORKDIR /build
COPY . .
RUN mvn clean package -DskipTests

FROM eclipse-temurin:8-jre-alpine
WORKDIR /app
COPY --from=builder /build/target/yy-api-gateway.jar app.jar
EXPOSE 8012 20881
ENTRYPOINT ["java", "-jar", "app.jar"]