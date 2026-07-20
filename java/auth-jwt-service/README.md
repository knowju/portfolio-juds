# auth-jwt-service

Servicio de **autenticación y autorización con JWT** sobre Spring Boot 3 + **Spring Security**. Registra usuarios, emite tokens firmados y protege endpoints por rol. Stateless (sin sesión de servidor). Contraseñas cifradas con BCrypt.

## Qué demuestra

- **Spring Security 6** con `SecurityFilterChain` (config moderna, sin `WebSecurityConfigurerAdapter`).
- **JWT** (jjwt): emisión, firma HMAC-SHA256 y validación en cada request.
- **Filtro `OncePerRequestFilter`** que autentica por token → app **stateless**.
- **Autorización por rol**: `/api/admin/**` solo para `ADMIN`.
- **BCrypt** para hashear contraseñas (nunca en claro).
- Manejo de errores 401/409/400 con `ProblemDetail`.

## Correr

```bash
cd java/auth-jwt-service
./gradlew bootRun          # http://localhost:8081
```

Usuario ADMIN de arranque: `admin` / `admin123`.

## Flujo de uso

1. **Login** para obtener el token:
```bash
curl -X POST http://localhost:8081/auth/login \
     -H "Content-Type: application/json" \
     -d '{"username":"admin","password":"admin123"}'
# → { "token": "eyJhbGciOiJIUzI1NiJ9..." }
```

2. **Llamar un endpoint protegido** con el token:
```bash
curl http://localhost:8081/api/me \
     -H "Authorization: Bearer <TOKEN>"
```

3. **Registrar** un usuario nuevo (rol USER, devuelve token):
```bash
curl -X POST http://localhost:8081/auth/register \
     -H "Content-Type: application/json" \
     -d '{"username":"ana","password":"secreto123"}'
```

4. Con el token de `ana` (rol USER), `/api/admin/panel` responde **403** (no autorizado); con el de `admin`, **200**.

## Endpoints

| Verbo | Ruta | Acceso |
|---|---|---|
| POST | `/auth/register` | Público |
| POST | `/auth/login` | Público |
| GET | `/api/me` | Autenticado |
| GET | `/api/admin/panel` | Solo ADMIN |

Swagger UI: http://localhost:8081/swagger-ui.html

## Nota de seguridad

El secreto JWT está en `application.properties` **solo para el demo**. En producción va en variable de entorno o secret manager, nunca en el repositorio.

## Stack

Java 21 · Spring Boot 3.3 · Spring Security 6 · jjwt 0.12 · JPA/Hibernate · H2 · BCrypt · Gradle
