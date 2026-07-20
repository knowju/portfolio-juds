# inventario-microservicios

Dos microservicios Spring Boot que se comunican por **eventos**: al crear un pedido, el servicio de inventario descuenta stock de forma **desacoplada** y **idempotente**.

```
┌──────────────────┐   PedidoCreado (evento)   ┌─────────────────────┐
│  pedidos-service │ ────────────────────────▶ │ inventario-service  │
│   :8082          │   POST /events/...        │   :8083             │
│  crea el pedido  │                           │  descuenta stock    │
└──────────────────┘                           └─────────────────────┘
```

## Qué demuestra

- **Arquitectura de microservicios** (dos servicios independientes, proyecto Gradle multi-módulo).
- **Event-Driven / event notification**: pedidos publica un evento; no espera ni conoce la lógica de inventario. Desacoplamiento real.
- **Publisher abstraído** (`EventPublisher` + `HttpEventPublisher`): cambiar HTTP por **Kafka/RabbitMQ/SNS** es reemplazar una clase, sin tocar la lógica de negocio.
- **Consumidor idempotente**: inventario guarda los `eventId` procesados; un evento repetido (reintento/redelivery) no descuenta doble.
- **Contratos, no librerías compartidas**: cada servicio define su propia copia del evento (evita el antipatrón del "monolito distribuido").

## Correr (dos terminales)

```bash
# Terminal 1 — inventario (arráncalo primero)
cd java/inventario-microservicios
./gradlew :inventario-service:bootRun     # :8083

# Terminal 2 — pedidos
cd java/inventario-microservicios
./gradlew :pedidos-service:bootRun        # :8082
```

## Probar el flujo

```bash
# 1. Ver stock inicial
curl http://localhost:8083/api/v1/inventario
# → {"laptop":10,"mouse":50,"teclado":30}

# 2. Crear un pedido de 3 laptops
curl -X POST http://localhost:8082/api/v1/pedidos \
     -H "Content-Type: application/json" \
     -d '{"producto":"laptop","cantidad":3}'

# 3. Ver stock: laptop bajó a 7 (inventario reaccionó al evento)
curl http://localhost:8083/api/v1/inventario
```

En la consola de inventario verás el log del evento procesado y, si el mismo `eventId` llega dos veces, el mensaje de idempotencia.

## Stack

Java 21 · Spring Boot 3.3 · Gradle multi-módulo · RestClient · arquitectura event-driven
