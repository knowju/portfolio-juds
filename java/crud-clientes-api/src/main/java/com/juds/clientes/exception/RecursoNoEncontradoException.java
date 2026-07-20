package com.juds.clientes.exception;

/**
 * Excepción de dominio para "no existe el recurso".
 * El GlobalExceptionHandler la traduce a un 404 limpio.
 */
public class RecursoNoEncontradoException extends RuntimeException {

    public RecursoNoEncontradoException(String mensaje) {
        super(mensaje);
    }
}
