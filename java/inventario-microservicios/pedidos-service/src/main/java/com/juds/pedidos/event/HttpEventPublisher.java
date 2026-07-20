package com.juds.pedidos.event;

import com.juds.pedidos.dto.PedidoDtos.PedidoCreadoEvento;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

/**
 * Implementación de EventPublisher que envía el evento por HTTP al servicio
 * de inventario (patrón "event notification"). Si el consumidor no responde,
 * se registra el fallo; en producción esto iría con reintentos + outbox.
 */
@Component
public class HttpEventPublisher implements EventPublisher {

    private static final Logger log = LoggerFactory.getLogger(HttpEventPublisher.class);

    private final RestClient restClient;

    public HttpEventPublisher(@Value("${inventario.url}") String inventarioUrl) {
        this.restClient = RestClient.create(inventarioUrl);
    }

    @Override
    public void publicarPedidoCreado(PedidoCreadoEvento evento) {
        try {
            restClient.post()
                    .uri("/events/pedido-creado")
                    .body(evento)
                    .retrieve()
                    .toBodilessEntity();
            log.info("Evento PedidoCreado publicado: {}", evento.eventId());
        } catch (Exception ex) {
            // En prod: reintentos / outbox. Aquí solo lo dejamos trazado.
            log.error("No se pudo publicar el evento {}: {}", evento.eventId(), ex.getMessage());
        }
    }
}
