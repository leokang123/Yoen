# ─── Build Stage ─────────────────────────────────────
FROM gradle:8.13-jdk17 AS builder
WORKDIR /app

# 1. Gradle wrapper와 설정파일 복사 (의존성 캐싱 목적)
COPY gradlew gradlew
COPY gradle gradle
COPY build.gradle.kts settings.gradle.kts ./
RUN chmod +x gradlew
RUN ./gradlew --no-daemon dependencies

# 2. 전체 프로젝트 복사 후 빌드
COPY . .
RUN ./gradlew --no-daemon bootJar

# ─── Runtime Stage ───────────────────────────────────
FROM eclipse-temurin:17-jre
WORKDIR /app

# 3. 빌드 결과물 JAR 복사
COPY --from=builder /app/build/libs/app.jar app.jar

# 4. 애플리케이션 실행
ENTRYPOINT ["java", "-jar", "app.jar"]
