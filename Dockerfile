# ---- build stage ----
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app

# 先拷貝 pom.xml 以利快取相依
COPY pom.xml ./
RUN mvn -q -DskipTests dependency:go-offline

# 再拷貝原始碼後建置
COPY src ./src
RUN mvn -DskipTests clean package

# ---- runtime stage ----
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

# 不用猜 jar 名稱
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]
