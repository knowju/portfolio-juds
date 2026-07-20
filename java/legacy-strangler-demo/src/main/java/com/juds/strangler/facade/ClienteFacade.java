package com.juds.strangler.facade;

import com.juds.strangler.acl.ClienteAntiCorruptionLayer;
import com.juds.strangler.domain.Cliente;
import com.juds.strangler.legacy.LegacyCliente;
import com.juds.strangler.legacy.LegacySistemaClientes;
import com.juds.strangler.modern.RepositorioClientesModerno;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * STRANGLER FIG FACADE.
 * Punto único de entrada que el cliente ve. Por dentro decide, operación por
 * operación, si responde el sistema NUEVO o el LEGACY (envuelto por la ACL).
 * Conforme se migran clientes, el legacy se "estrangula" hasta poder retirarse,
 * sin que el consumidor note el cambio.
 *
 * Reglas del demo:
 *  - Lecturas: primero el nuevo; si no está, se cae al legacy (traducido por ACL).
 *  - Altas: SIEMPRE en el sistema nuevo (los clientes nuevos nacen migrados).
 *  - migrar(id): copia un cliente del legacy al nuevo y lo saca del legacy.
 */
@Service
public class ClienteFacade {

    private final LegacySistemaClientes legacy;
    private final ClienteAntiCorruptionLayer acl;
    private final RepositorioClientesModerno moderno;
    private final AtomicInteger secuencia = new AtomicInteger(100);

    public ClienteFacade(LegacySistemaClientes legacy, ClienteAntiCorruptionLayer acl,
                         RepositorioClientesModerno moderno) {
        this.legacy = legacy;
        this.acl = acl;
        this.moderno = moderno;
    }

    public Cliente buscar(String id) {
        return moderno.buscar(id)
                .or(() -> legacy.buscar(id).map(acl::traducir))
                .orElseThrow(() -> new NoSuchElementException("Cliente " + id + " no existe"));
    }

    public List<Cliente> listar() {
        List<Cliente> resultado = new ArrayList<>(moderno.listar());
        for (LegacyCliente lc : legacy.listar()) {
            if (!moderno.existe(lc.CUSTOMER_NUMBER())) {
                resultado.add(acl.traducir(lc));   // los que aún viven en el legacy
            }
        }
        return resultado;
    }

    public Cliente crear(String nombre, String email) {
        String id = "N" + secuencia.getAndIncrement();
        Cliente nuevo = new Cliente(id, nombre, email, true, LocalDate.now());
        return moderno.guardar(nuevo);   // nace en el sistema nuevo
    }

    /** Migra un cliente del legacy al sistema nuevo (a través de la ACL). */
    public Cliente migrar(String id) {
        if (moderno.existe(id)) {
            throw new IllegalStateException("El cliente " + id + " ya está migrado");
        }
        LegacyCliente lc = legacy.buscar(id)
                .orElseThrow(() -> new NoSuchElementException("Cliente " + id + " no existe en el legacy"));

        Cliente migrado = moderno.guardar(acl.traducir(lc));
        legacy.eliminar(id);   // el legacy se encoge: strangler en acción
        return migrado;
    }

    public Map<String, Integer> estadoMigracion() {
        return Map.of(
                "enLegacy", legacy.cuenta(),
                "enSistemaNuevo", moderno.cuenta(),
                "total", legacy.cuenta() + moderno.cuenta());
    }

    /** Excepción local para "no encontrado" (evita import extra en este demo). */
    public static class NoSuchElementException extends RuntimeException {
        public NoSuchElementException(String m) {
            super(m);
        }
    }
}
