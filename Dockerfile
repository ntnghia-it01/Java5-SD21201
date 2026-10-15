# syntax=docker/dockerfile:1
# Giữ Java 21 và WAR của project; không cần Maven trên VPS.
FROM eclipse-temurin:21-jdk-jammy AS build
WORKDIR /build
COPY .mvn/ .mvn/
COPY mvnw pom.xml ./
RUN chmod +x mvnw
COPY src/ src/
# contextLoads cần DB thật: chạy kiểm thử tích hợp riêng, không trong image build.
RUN --mount=type=cache,target=/root/.m2 ./mvnw -B -DskipTests package \
    && cp target/Java5-*.war /build/app.war

FROM eclipse-temurin:21-jre-jammy AS runtime
RUN apt-get update \
    && apt-get install -y --no-install-recommends curl \
    && rm -rf /var/lib/apt/lists/* \
    && groupadd --gid 10001 app \
    && useradd --uid 10001 --gid app --no-create-home app \
    && mkdir -p /app/images-upload \
    && chown -R app:app /app
WORKDIR /app
COPY --from=build --chown=app:app /build/app.war ./app.war
USER app
EXPOSE 8080
HEALTHCHECK --interval=15s --timeout=5s --start-period=90s --retries=5 \
    CMD curl --fail --silent http://127.0.0.1:8080/login > /dev/null || exit 1
ENTRYPOINT ["java", "-jar", "/app/app.war"]
