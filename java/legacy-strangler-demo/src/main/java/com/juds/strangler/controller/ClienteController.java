package com.juds.strangler.controller;

import com.juds.strangler.domain.Cliente;
import com.juds.strangler.facade.ClienteFacade;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * El consumidor ve UNA sola API. No sabe (ni le importa) si detrás responde el
 * sistema nuevo o el legacy: eso lo resuelve el facade.
 */
@RestController
@RequestMapping("/api/v1/clientes")
public class ClienteController {

    private final ClienteFacade facade;

    public ClienteController(ClienteFacade facade) {
        this.facade = facade;
    }

    @GetMapping
    public List<Cliente> listar() {
        return facade.listar();
    }

    @GetMapping("/{id}")
    public Cliente obtener(@PathVariable String id) {
        return facade.buscar(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Cliente crear(@Valid @RequestBody ClienteRequest req) {
        return facade.crear(req.nombre(), req.email());
    }

    public record ClienteRequest(
            @NotBlank String nombre,
            @NotBlank @Email String email) {
    }
}
