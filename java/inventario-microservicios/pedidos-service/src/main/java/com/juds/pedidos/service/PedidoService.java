package com.juds.pedidos.service;

import com.juds.pedidos.dto.PedidoDtos.PedidoCreadoEvento;
import com.juds.pedidos.dto.PedidoDtos.PedidoRequest;
import com.juds.pedidos.dto.PedidoDtos.PedidoResponse;
import com.juds.pedidos.event.EventPublisher;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Crea el pedido y publica el evento. El servicio de inventario reacciona
 * de forma asíncrona/desacoplada: pedidos no sabe qué pasa después.
 */
@Service
public class PedidoService {

    private final EventPublisher eventPublisher;
    private final AtomicLong secuencia = new AtomicLong(1);

    public PedidoService(EventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    public PedidoResponse crear(PedidoRequest req) {
        Long pedidoId = secuencia.getAndIncrement();

        PedidoCreadoEvento evento = new PedidoCreadoEvento(
                UUID.randomUUID().toString(), pedidoId, req.producto(), req.cantidad());
        eventPublisher.publicarPedidoCreado(evento);

        return new PedidoResponse(pedidoId, req.producto(), req.cantidad(), "CREADO");
    }
}
