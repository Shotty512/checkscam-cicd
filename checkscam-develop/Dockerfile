# syntax=docker/dockerfile:1.7


# Stage 1: Build

FROM eclipse-temurin:17-jdk-jammy AS builder

WORKDIR /app

# Gradle configuration
ENV GRADLE_USER_HOME=/root/.gradle

# Copy only files required to resolve dependencies first.
# This allows Docker to reuse the dependency cache when source
# code changes but build.gradle remains unchanged.
COPY gradlew .
COPY gradle gradle
COPY build.gradle settings.gradle* gradle.properties* ./

RUN chmod +x ./gradlew

# Download/resolve dependencies.
# BuildKit cache prevents downloading Gradle dependencies again.
RUN ./gradlew dependencies --no-daemon
# Copy source code only after dependency layer.
COPY src src

# Build Spring Boot executable JAR.
RUN ./gradlew bootJar --no-daemon
# Stage 2: Runtime

FROM eclipse-temurin:17-jre-jammy AS runtime

WORKDIR /app

# Application configuration
ENV SERVER_PORT=8080 \
    JAVA_TOOL_OPTIONS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=70.0 -XX:InitialRAMPercentage=25.0 -XX:MinRAMPercentage=25.0 -XX:+ExitOnOutOfMemoryError"

# Create non-root user for security
RUN groupadd --system spring \
    && useradd --system --gid spring --home-dir /app spring

# Copy only the generated application.
# Source code, Gradle and build tools are NOT included.
COPY --from=builder /app/build/libs/*.jar /app/app.jar

RUN chown -R spring:spring /app

USER spring:spring

EXPOSE 8080

# Spring Boot Actuator health endpoint
HEALTHCHECK --interval=30s \
            --timeout=5s \
            --start-period=60s \
            --retries=3 \
            CMD wget --no-verbose --tries=1 --spider \
            http://127.0.0.1:8080/actuator/health \
            || exit 1

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
