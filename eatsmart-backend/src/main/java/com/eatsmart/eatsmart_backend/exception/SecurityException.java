package com.eatsmart.eatsmart_backend.exception;

/**
 * Excepción para errores de seguridad
 * Se lanza cuando hay intentos de acceso no autorizado
 */
public class SecurityException extends RuntimeException {

    public SecurityException(String message) {
        super(message);
    }

    public SecurityException(String message, Throwable cause) {
        super(message, cause);
    }
}