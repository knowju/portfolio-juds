package com.juds.clientes.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * DTO de entrada. Separar el request de la entidad evita exponer la BD
 * directamente y permite validar el payload antes de tocar la lógica.
 * (record de Java = clase inmutable con getters, equals, toString generados).
 */
public record ClienteRequest(

        @NotBlank(message = "el nombre es obligatorio")
        String nombre,

        @NotBlank(message = "el email es obligatorio")
        @Email(message = "el email no es válido")
        String email,

        Boolean activo
) {
}
