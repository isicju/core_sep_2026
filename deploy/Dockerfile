FROM eclipse-temurin:21-jdk AS builder

RUN apt-get update && \
    apt-get install -y --no-install-recommends git && \
    rm -rf /var/lib/apt/lists/*

WORKDIR /app

RUN git clone --branch hw2_solution  https://github.com/isicju/core_sep_2026.git .


RUN chmod +x gradlew && \
    ./gradlew clean build

# Runtime stage
FROM eclipse-temurin:21-jre

WORKDIR /app

COPY --from=builder /app/build/libs/gradle_based-1.0-SNAPSHOT-all.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
