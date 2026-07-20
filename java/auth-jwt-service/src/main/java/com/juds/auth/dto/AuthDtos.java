package com.juds.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTOs de autenticación agrupados. Son records inmutables con validación.
 */
public final class AuthDtos {

    private AuthDtos() {
    }

    public record RegisterRequest(
            @NotBlank String username,
            @NotBlank @Size(min = 6, message = "la contraseña debe tener al menos 6 caracteres") String password) {
    }

    public record LoginRequest(
            @NotBlank String username,
            @NotBlank String password) {
    }

    public record AuthResponse(String token) {
    }
}
