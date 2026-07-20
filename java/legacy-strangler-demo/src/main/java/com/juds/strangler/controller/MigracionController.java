package com.juds.strangler.controller;

import com.juds.strangler.domain.Cliente;
import com.juds.strangler.facade.ClienteFacade;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Endpoints operativos de la migración: ver el avance y migrar un cliente.
 * Cada migración encoge el legacy y crece el sistema nuevo.
 */
@RestController
@RequestMapping("/api/v1/migracion")
public class MigracionController {

    private final ClienteFacade facade;

    public MigracionController(ClienteFacade facade) {
        this.facade = facade;
    }

    @GetMapping("/estado")
    public Map<String, Integer> estado() {
        return facade.estadoMigracion();
    }

    @PostMapping("/{id}")
    public Cliente migrar(@PathVariable String id) {
        return facade.migrar(id);
    }
}
