package com.juds.strangler.legacy;

/**
 * Modelo tal como lo expone el sistema LEGACY: nombres tipo mainframe,
 * estado como código ("A"/"I"), fecha como String "yyyyMMdd".
 * Feo a propósito: es lo que la Anti-Corruption Layer tiene que traducir
 * para que esa fealdad NO se filtre al dominio nuevo.
 */
public record LegacyCliente(
        String CUSTOMER_NUMBER,
        String CUSTOMER_NAME,
        String EMAIL_ADDR,
        String STATUS_CD,     // "A" = activo, "I" = inactivo
        String REG_DATE       // "yyyyMMdd"
) {
}
