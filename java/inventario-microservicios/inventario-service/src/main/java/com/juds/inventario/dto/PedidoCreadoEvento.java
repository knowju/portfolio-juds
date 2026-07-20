package com.juds.inventario.dto;

/**
 * Copia local del contrato del evento. Cada servicio define su propia versión
 * (no compartimos una librería de modelos: eso acoplaría los servicios y es un
 * antipatrón del "monolito distribuido"). Basta con que el JSON case.
 */
public record PedidoCreadoEvento(String eventId, Long pedidoId, String producto, int cantidad) {
}
