package com.juds.clientes.repository;

import com.juds.clientes.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Solo declaramos la interfaz: Spring Data JPA genera la implementación en runtime.
 * Hereda findAll, findById, save, deleteById, etc. sin escribir SQL.
 * Los métodos "derivados" (findBy...) se traducen a queries por el nombre.
 */
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    List<Cliente> findByActivoTrue();               // SELECT * FROM clientes WHERE activo = true

    Optional<Cliente> findByEmail(String email);    // SELECT * FROM clientes WHERE email = ?

    boolean existsByEmail(String email);
}
