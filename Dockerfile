FROM eclipse-temurin:17-jdk-alpine

# Instalar certificados
RUN apk add --no-cache ca-certificates

# Copiar o JAR da aplicação (usando caminho específico)
COPY /home/runner/work/order-management/order-management/target/*.jar app.jar

# Executar a aplicação
ENTRYPOINT ["java", "-jar", "/app.jar"] 