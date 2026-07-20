package com.juds.strangler.acl;

import com.juds.strangler.domain.Cliente;
import com.juds.strangler.legacy.LegacyCliente;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * ANTI-CORRUPTION LAYER.
 * Único punto donde se conoce el formato del legacy. Traduce el modelo viejo
 * al dominio nuevo: código de estado → boolean, String "yyyyMMdd" → LocalDate,
 * nombres de campo feos → nombres claros.
 *
 * Beneficio: si el legacy cambia, solo se toca esta clase. El resto del sistema
 * nuevo queda protegido de la "corrupción" del modelo antiguo.
 */
@Component
public class ClienteAntiCorruptionLayer {

    private static final DateTimeFormatter FORMATO_LEGACY = DateTimeFormatter.ofPattern("yyyyMMdd");

    public Cliente traducir(LegacyCliente legacy) {
        boolean activo = "A".equalsIgnoreCase(legacy.STATUS_CD());
        LocalDate fecha = LocalDate.parse(legacy.REG_DATE(), FORMATO_LEGACY);
        String nombreLimpio = normalizarNombre(legacy.CUSTOMER_NAME());

        return new Cliente(
                legacy.CUSTOMER_NUMBER(),
                nombreLimpio,
                legacy.EMAIL_ADDR(),
                activo,
                fecha);
    }

    /** El legacy guarda nombres en MAYÚSCULAS; el dominio nuevo los quiere legibles. */
    private String normalizarNombre(String enMayusculas) {
        String[] palabras = enMayusculas.toLowerCase().split("\\s+");
        StringBuilder sb = new StringBuilder();
        for (String p : palabras) {
            if (!p.isEmpty()) {
                sb.append(Character.toUpperCase(p.charAt(0))).append(p.substring(1)).append(' ');
            }
        }
        return sb.toString().trim();
    }
}
