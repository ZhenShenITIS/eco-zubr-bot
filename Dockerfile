# ===== ЭТАП 1: СБОРКА (builder) =====
FROM eclipse-temurin:21-jdk-jammy AS builder

WORKDIR /build

# Устанавливаем Git и Maven
RUN apt-get update && apt-get install -y git maven

# ===== СКАЧИВАЕМ И СОБИРАЕМ ПЕРВУЮ БИБЛИОТЕКУ =====
RUN git clone https://github.com/Moriec/max-bot-api-client-java.git /build/library1

WORKDIR /build/library1
RUN mvn clean install -DskipTests

# ===== СКАЧИВАЕМ И СОБИРАЕМ ВТОРУЮ БИБЛИОТЕКУ =====
RUN git clone https://github.com/Moriec/max-bot-sdk-java.git /build/library2

WORKDIR /build/library2
RUN mvn clean install -DskipTests

# ===== СКАЧИВАЕМ И СОБИРАЕМ ОСНОВНОЕ ПРИЛОЖЕНИЕ =====
WORKDIR /build/app

COPY pom.xml .
COPY src ./src

# Собираем приложение
RUN mvn clean package -DskipTests

# ===== ЭТАП 2: RUNTIME (финальный образ) =====
FROM eclipse-temurin:21-jre-jammy

WORKDIR /app

# Копируем только готовый JAR
COPY --from=builder /build/app/target/*.jar app.jar

# КОПИРУЕМ СТАТИЧЕСКИЕ ФАЙЛЫ (ДОБАВИТЬ ЭТУ СТРОКУ!)
COPY --from=builder /build/app/src/main/resources/static/ /app/src/main/resources/static/

# Expose порт Spring Boot (по умолчанию 8080)
EXPOSE 8080

ENV SPRING_DATASOURCE_URL=""
ENV SPRING_DATASOURCE_USERNAME=""
ENV SPRING_DATASOURCE_PASSWORD=""
ENV SPRING_JPA_HIBERNATE_DDL_AUTO=update

ENV MAX_TOKEN=""
ENV YANDEX_GEOCODER_TOKEN=""

# Команда запуска контейнера
ENTRYPOINT ["java", "-jar", "app.jar"]
