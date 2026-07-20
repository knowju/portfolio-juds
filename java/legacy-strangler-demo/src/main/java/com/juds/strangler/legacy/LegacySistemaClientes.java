package com.juds.strangler.legacy;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

/**
 * Simula el sistema legacy (una BD vieja / mainframe / SOAP). No lo tocamos:
 * lo envolvemos. En una migración real esto sería un cliente SOAP, un JDBC a
 * la BD antigua, o una API que no controlas.
 */
@Component
public class LegacySistemaClientes {

    private final Map<String, LegacyCliente> datos = new ConcurrentHashMap<>();

    public LegacySistemaClientes() {
        datos.put("C001", new LegacyCliente("C001", "ANA TORRES", "ana@mail.com", "A", "20190115"));
        datos.put("C002", new LegacyCliente("C002", "LUIS PEREZ", "luis@mail.com", "I", "20201203"));
        datos.put("C003", new LegacyCliente("C003", "MARTA RIOS", "marta@mail.com", "A", "20180620"));
    }

    public Optional<LegacyCliente> buscar(String customerNumber) {
        return Optional.ofNullable(datos.get(customerNumber));
    }

    public List<LegacyCliente> listar() {
        return new ArrayList<>(datos.values());
    }

    /** Cuando un cliente se migra al sistema nuevo, deja de vivir en el legacy. */
    public void eliminar(String customerNumber) {
        datos.remove(customerNumber);
    }

    public int cuenta() {
        return datos.size();
    }
}
