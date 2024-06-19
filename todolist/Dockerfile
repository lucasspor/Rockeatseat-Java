# Primeira etapa: construção
FROM openjdk:17-jdk AS builder
WORKDIR /app

RUN apt-get update && \
    apt-get install -y maven

COPY . .
RUN mvn clean install

# Segunda etapa: imagem final
FROM openjdk:17-jdk-slim
EXPOSE 8080

COPY --from=builder /app/target/todolist-1.0.0.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]