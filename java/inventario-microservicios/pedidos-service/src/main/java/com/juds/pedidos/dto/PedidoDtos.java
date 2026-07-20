package com.juds.pedidos.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public final class PedidoDtos {

    private PedidoDtos() {
    }

    /** Lo que el cliente manda para crear un pedido. */
    public record PedidoRequest(
            @NotBlank String producto,
            @NotNull @Min(1) Integer cantidad) {
    }

    /** Respuesta al crear el pedido. */
    public record PedidoResponse(Long pedidoId, String producto, int cantidad, String estado) {
    }

    /**
     * Evento de dominio que se publica hacia el servicio de inventario.
     * eventId permite al consumidor ser idempotente (no procesar dos veces).
     * Es el CONTRATO entre servicios; cada servicio lo define por su cuenta.
     */
    public record PedidoCreadoEvento(String eventId, Long pedidoId, String producto, int cantidad) {
    }
}
