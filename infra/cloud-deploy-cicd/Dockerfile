# Imagen multi-stage para la API Spring Boot (crud-clientes-api).
# Contexto de build esperado: java/crud-clientes-api
#   docker build -f infra/cloud-deploy-cicd/Dockerfile -t crud-clientes-api java/crud-clientes-api

# --- etapa build: compila el jar dentro de la imagen (build reproducible) ---
FROM eclipse-temurin:21-jdk AS build
WORKDIR /app
COPY . .
RUN chmod +x gradlew && ./gradlew clean bootJar --no-daemon

# --- etapa runtime: imagen ligera solo con el JRE y el jar ---
FROM eclipse-temurin:21-jre
WORKDIR /app
# usuario no-root por seguridad
RUN useradd -r -u 1001 appuser
COPY --from=build /app/build/libs/*.jar app.jar
USER appuser
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
