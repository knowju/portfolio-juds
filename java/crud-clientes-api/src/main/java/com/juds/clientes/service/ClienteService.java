package com.juds.clientes.service;

import com.juds.clientes.dto.ClienteRequest;
import com.juds.clientes.exception.RecursoNoEncontradoException;
import com.juds.clientes.model.Cliente;
import com.juds.clientes.repository.ClienteRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Capa de negocio. El controller nunca toca el repositorio directo:
 * las reglas (validar email duplicado, "no encontrado", etc.) viven aquí.
 */
@Service
public class ClienteService {

    private final ClienteRepository repo;

    public ClienteService(ClienteRepository repo) {   // inyección por constructor
        this.repo = repo;
    }

    public List<Cliente> listar() {
        return repo.findAll();
    }

    public Cliente buscarPorId(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Cliente con id " + id + " no encontrado"));
    }

    public Cliente crear(ClienteRequest req) {
        if (repo.existsByEmail(req.email())) {
            throw new IllegalArgumentException("Ya existe un cliente con email " + req.email());
        }
        boolean activo = req.activo() == null || req.activo();
        return repo.save(new Cliente(req.nombre(), req.email(), activo));
    }

    public Cliente actualizar(Long id, ClienteRequest req) {
        Cliente cliente = buscarPorId(id);
        cliente.setNombre(req.nombre());
        cliente.setEmail(req.email());
        if (req.activo() != null) {
            cliente.setActivo(req.activo());
        }
        return repo.save(cliente);
    }

    public void eliminar(Long id) {
        Cliente cliente = buscarPorId(id);   // lanza 404 si no existe
        repo.delete(cliente);
    }
}
