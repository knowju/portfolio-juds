package com.juds.inventario.service;

import com.juds.inventario.dto.PedidoCreadoEvento;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Mantiene el stock y reacciona al evento PedidoCreado descontando existencias.
 *
 * Consumidor IDEMPOTENTE: guarda los eventId ya procesados. Si el mismo evento
 * llega dos veces (reintentos, redelivery), no se descuenta doble. Esta es una
 * propiedad clave de cualquier consumidor de eventos en producción.
 */
@Service
public class InventarioService {

    private static final Logger log = LoggerFactory.getLogger(InventarioService.class);

    private final Map<String, Integer> stock = new ConcurrentHashMap<>(Map.of(
            "laptop", 10,
            "mouse", 50,
            "teclado", 30));

    private final Set<String> eventosProcesados = ConcurrentHashMap.newKeySet();

    public synchronized void procesarPedidoCreado(PedidoCreadoEvento evento) {
        if (!eventosProcesados.add(evento.eventId())) {
            log.info("Evento {} ya procesado, se ignora (idempotencia)", evento.eventId());
            return;
        }

        stock.merge(evento.producto(), -evento.cantidad(), Integer::sum);
        log.info("Pedido {} procesado: -{} de '{}'. Stock restante: {}",
                evento.pedidoId(), evento.cantidad(), evento.producto(), stock.get(evento.producto()));
    }

    public Map<String, Integer> obtenerStock() {
        return Map.copyOf(stock);
    }
}
