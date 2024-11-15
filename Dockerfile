FROM eclipse-temurin:17-jdk-alpine

# Instalar certificados
RUN apk add --no-cache ca-certificates

# Copiar o JAR da aplicação
COPY target/*.jar app.jar

# Executar a aplicação
ENTRYPOINT ["java", "-jar", "/app.jar"] 