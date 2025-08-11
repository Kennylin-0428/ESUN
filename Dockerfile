# 使用 OpenJDK 17 作為基礎映像檔
FROM openjdk:17-jdk-slim

# 設定工作目錄
WORKDIR /app

# 將 Maven 專案的 JAR 檔複製到容器中
# 這裡假設 mvn clean package 已經執行過，且 JAR 檔在 target/ 目錄下
COPY target/demo-0.0.1-SNAPSHOT.jar app.jar

# 暴露應用程式使用的埠
EXPOSE 8080

# 啟動應用程式
ENTRYPOINT ["java", "-jar", "app.jar"]
