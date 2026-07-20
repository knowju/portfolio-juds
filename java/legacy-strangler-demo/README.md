# legacy-strangler-demo

Demostración de **modernización de un sistema legacy hacia una arquitectura nueva** usando dos patrones clave de migración: **Strangler Fig** y **Anti-Corruption Layer (ACL)**. Es el escenario directo de la parte *Cloud & Migrations* del puesto.

```
                    ┌─────────────────────────────┐
   cliente  ──────▶ │   ClienteFacade (Strangler) │
   (una sola API)   └──────────┬──────────┬───────┘
                               │          │
                     ¿migrado? │          │ aún no
                               ▼          ▼
                    ┌──────────────┐   ┌───────────────┐
                    │ Sistema NUEVO│   │  ACL (traduce) │
                    │ (dominio     │   └──────┬────────┘
                    │  limpio)     │          ▼
                    └──────────────┘   ┌───────────────┐
                                       │ Sistema LEGACY│
                                       │ (modelo feo)  │
                                       └───────────────┘
```

## Qué demuestra

- **Strangler Fig**: un `facade` es el único punto de entrada. Enruta cada operación al sistema nuevo o al legacy. Conforme se migran clientes, el legacy se encoge hasta poder retirarse — **sin downtime ni big-bang rewrite**, y sin que el consumidor note el cambio.
- **Anti-Corruption Layer**: una capa única traduce el modelo legacy (código de estado `"A"/"I"`, fecha `String "yyyyMMdd"`, nombres tipo mainframe) al **dominio limpio** (`boolean`, `LocalDate`, nombres claros). La fealdad del legacy **no se filtra** al sistema nuevo; si el legacy cambia, solo se toca la ACL.
- **Migración incremental y reversible**, medible con un endpoint de estado.

## Correr

```bash
cd java/legacy-strangler-demo
./gradlew bootRun          # http://localhost:8084
```

Al arrancar hay 3 clientes en el legacy (`C001`, `C002`, `C003`) y 0 en el sistema nuevo.

## Ver el estrangulamiento en acción

```bash
# Estado inicial: 3 en legacy, 0 en nuevo
curl http://localhost:8084/api/v1/migracion/estado

# Leer C001: viene del legacy, pero ya traducido (activo:true, fecha ISO, nombre "Ana Torres")
curl http://localhost:8084/api/v1/clientes/C001

# Migrar C001 al sistema nuevo (pasa por la ACL y sale del legacy)
curl -X POST http://localhost:8084/api/v1/migracion/C001

# Estado: 2 en legacy, 1 en nuevo — el legacy se encogió
curl http://localhost:8084/api/v1/migracion/estado

# Alta de un cliente nuevo: nace directo en el sistema nuevo
curl -X POST http://localhost:8084/api/v1/clientes \
     -H "Content-Type: application/json" \
     -d '{"nombre":"Nuevo Cliente","email":"nuevo@mail.com"}'

# Listar: unifica nuevo + legacy, el consumidor ve una sola lista coherente
curl http://localhost:8084/api/v1/clientes
```

## Por qué importa para el puesto

Migrar un monolito a la nube (AWS) rara vez es un rewrite de golpe. Se hace **estrangulando** el legacy pieza por pieza, con una **ACL** en la frontera para que el nuevo sistema no herede el modelo viejo. Este repo es esa idea en pequeño y ejecutable.

## Stack

Java 21 · Spring Boot 3.3 · patrones Strangler Fig + Anti-Corruption Layer · Gradle
