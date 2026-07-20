package com.juds.strangler.modern;

import com.juds.strangler.domain.Cliente;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Almacén del sistema NUEVO (en memoria para el demo; en real sería Postgres/JPA).
 * Guarda el modelo de dominio limpio.
 */
@Repository
public class RepositorioClientesModerno {

    private final Map<String, Cliente> datos = new ConcurrentHashMap<>();

    public Optional<Cliente> buscar(String id) {
        return Optional.ofNullable(datos.get(id));
    }

    public boolean existe(String id) {
        return datos.containsKey(id);
    }

    public Cliente guardar(Cliente cliente) {
        datos.put(cliente.id(), cliente);
        return cliente;
    }

    public List<Cliente> listar() {
        return List.copyOf(datos.values());
    }

    public int cuenta() {
        return datos.size();
    }
}
