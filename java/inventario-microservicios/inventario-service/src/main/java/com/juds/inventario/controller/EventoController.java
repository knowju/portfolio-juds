package com.juds.inventario.controller;

import com.juds.inventario.dto.PedidoCreadoEvento;
import com.juds.inventario.service.InventarioService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * Punto de entrada de los eventos que publica el servicio de pedidos.
 * Aquí "consumimos" el evento. En prod este endpoint sería un listener de
 * Kafka/RabbitMQ/SQS en vez de un POST HTTP.
 */
@RestController
@RequestMapping("/events")
public class EventoController {

    private final InventarioService service;

    public EventoController(InventarioService service) {
        this.service = service;
    }

    @PostMapping("/pedido-creado")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void onPedidoCreado(@RequestBody PedidoCreadoEvento evento) {
        service.procesarPedidoCreado(evento);
    }
}
