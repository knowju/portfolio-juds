package com.juds.strangler.domain;

import java.time.LocalDate;

/**
 * Modelo de dominio LIMPIO del sistema nuevo. Nombres claros, tipos correctos
 * (boolean, LocalDate). Ni el código de estado ni el String de fecha del legacy
 * llegan hasta aquí: la ACL los traduce antes.
 */
public record Cliente(
        String id,
        String nombre,
        String email,
        boolean activo,
        LocalDate fechaRegistro
) {
}
