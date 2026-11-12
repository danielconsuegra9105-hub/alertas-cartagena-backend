# ---- Build ----
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn -q dependency:go-offline
COPY src ./src
RUN mvn -q clean package -DskipTests

# ---- Run ----
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
# Render free tier = 512 MB RAM
ENV JAVA_TOOL_OPTIONS="-Xmx350m -XX:+UseSerialGC -Duser.timezone=America/Bogota"
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
