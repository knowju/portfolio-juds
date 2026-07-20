# crud-clientes-api

API REST de gestión de clientes construida con **Spring Boot 3 + JPA/Hibernate**, con documentación **OpenAPI/Swagger** y manejo centralizado de errores. Base de datos H2 en memoria para correr sin instalar nada; configuración lista para PostgreSQL.

## Qué demuestra

- Arquitectura en capas **Controller → Service → Repository → Entity**.
- Inyección de dependencias por constructor (IoC/DI).
- Persistencia con Spring Data JPA (repositorios derivados, sin SQL manual).
- Validación de payload con Bean Validation (`@Valid`).
- Manejo de errores con `@RestControllerAdvice` y `ProblemDetail` (RFC 7807).
- Documentación automática de la API (OpenAPI 3).

## Correr

```bash
cd java/crud-clientes-api
./gradlew bootRun          # levanta en http://localhost:8080
```

## Endpoints

| Verbo | Ruta | Descripción |
|---|---|---|
| GET | `/api/v1/clientes` | Lista todos |
| GET | `/api/v1/clientes/{id}` | Uno por id (404 si no existe) |
| POST | `/api/v1/clientes` | Crea (201 + Location; 409 si email duplicado) |
| PUT | `/api/v1/clientes/{id}` | Actualiza |
| DELETE | `/api/v1/clientes/{id}` | Borra (204) |

## Explorar

- **Swagger UI:** http://localhost:8080/swagger-ui.html
- **Consola H2:** http://localhost:8080/h2-console (JDBC URL: `jdbc:h2:mem:clientesdb`, user `sa`)

## Probar con curl

```bash
curl http://localhost:8080/api/v1/clientes

curl -X POST http://localhost:8080/api/v1/clientes \
     -H "Content-Type: application/json" \
     -d '{"nombre":"Marta","email":"marta@mail.com","activo":true}'
```

## Stack

Java 21 · Spring Boot 3.3 · Spring Data JPA · Hibernate · H2 / PostgreSQL · springdoc-openapi · Gradle
