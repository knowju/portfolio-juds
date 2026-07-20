package com.juds.pedidos.event;

import com.juds.pedidos.dto.PedidoDtos.PedidoCreadoEvento;

/**
 * Abstracción del transporte de eventos. En el demo se publica por HTTP,
 * pero la lógica de negocio no lo sabe: cambiar a Kafka/RabbitMQ/SNS es
 * reemplazar la implementación, sin tocar el PedidoService.
 */
public interface EventPublisher {

    void publicarPedidoCreado(PedidoCreadoEvento evento);
}
