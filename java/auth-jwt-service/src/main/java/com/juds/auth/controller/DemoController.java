package com.juds.auth.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Endpoints protegidos para demostrar la autorización:
 * - /api/me: cualquier usuario autenticado (con token válido).
 * - /api/admin/panel: solo rol ADMIN (lo fuerza SecurityConfig).
 */
@RestController
@RequestMapping("/api")
public class DemoController {

    @GetMapping("/me")
    public Map<String, Object> yo(Authentication auth) {
        return Map.of(
                "usuario", auth.getName(),
                "roles", auth.getAuthorities());
    }

    @GetMapping("/admin/panel")
    public Map<String, String> panelAdmin() {
        return Map.of("mensaje", "Bienvenido al panel de administración (solo ADMIN)");
    }
}
