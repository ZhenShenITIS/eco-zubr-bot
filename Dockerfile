# ===== ЭТАП 1: СБОРКА (builder) =====
FROM eclipse-temurin:21-jdk-jammy AS builder

WORKDIR /build

RUN apt-get update && apt-get install -y git maven

RUN git clone https://github.com/Moriec/max-bot-api-client-java.git /build/library1

WORKDIR /build/library1
RUN mvn clean install -DskipTests

RUN git clone https://github.com/Moriec/max-bot-sdk-java.git /build/library2

WORKDIR /build/library2
RUN mvn clean install -DskipTests

WORKDIR /build/app

COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests

# ===== ЭТАП 2: RUNTIME  =====
FROM eclipse-temurin:21-jre-jammy

WORKDIR /app

COPY --from=builder /build/app/target/*.jar app.jar

COPY --from=builder /build/app/src/main/resources/static/ /app/src/main/resources/static/

EXPOSE 8080

ENV SPRING_DATASOURCE_URL=""
ENV SPRING_DATASOURCE_USERNAME=""
ENV SPRING_DATASOURCE_PASSWORD=""
ENV SPRING_JPA_HIBERNATE_DDL_AUTO=update

ENV MAX_TOKEN=""
ENV YANDEX_GEOCODER_TOKEN=""

ENTRYPOINT ["java", "-jar", "app.jar"]
