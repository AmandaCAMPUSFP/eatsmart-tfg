package com.eatsmart.eatsmart_backend;

import com.eatsmart.eatsmart_backend.service.PasswordValidationService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests del servicio de validación de contraseñas.
 *
 * Verifica el cumplimiento de OWASP A07:2021 - Identification and
 * Authentication Failures: política de contraseñas robusta.
 */
class PasswordValidationServiceTest {

    private final PasswordValidationService service = new PasswordValidationService();

    @Test
    @DisplayName("Una contraseña fuerte es aceptada")
    void contrasenaFuerteEsValida() {
        // 8+ chars, mayúscula, minúscula, dígito y símbolo
        assertDoesNotThrow(() -> service.validarContraseña("Password123!"),
                "Una contraseña que cumple todos los requisitos no debe lanzar excepción");
    }

    @Test
    @DisplayName("Una contraseña demasiado corta es rechazada")
    void contrasenaCortaEsRechazada() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> service.validarContraseña("Ab1!"),
                "Una contraseña de menos de 8 caracteres debe lanzar excepción");
        assertTrue(ex.getMessage().contains("8 caracteres"));
    }

    @Test
    @DisplayName("Una contraseña sin mayúsculas es rechazada")
    void contrasenaSinMayusculaEsRechazada() {
        assertThrows(IllegalArgumentException.class,
                () -> service.validarContraseña("password123!"),
                "Sin mayúscula debe lanzar excepción");
    }

    @Test
    @DisplayName("Una contraseña sin dígitos es rechazada")
    void contrasenaSinDigitoEsRechazada() {
        assertThrows(IllegalArgumentException.class,
                () -> service.validarContraseña("Password!"),
                "Sin dígito debe lanzar excepción");
    }

    @Test
    @DisplayName("Una contraseña sin carácter especial es rechazada")
    void contrasenaSinEspecialEsRechazada() {
        assertThrows(IllegalArgumentException.class,
                () -> service.validarContraseña("Password123"),
                "Sin carácter especial debe lanzar excepción");
    }

    @Test
    @DisplayName("Una contraseña nula es rechazada")
    void contrasenaNulaEsRechazada() {
        assertThrows(IllegalArgumentException.class,
                () -> service.validarContraseña(null),
                "Una contraseña nula debe lanzar excepción");
    }

    @Test
    @DisplayName("La fortaleza de una contraseña completa es máxima (5)")
    void fortalezaMaximaEsCinco() {
        int strength = service.getPasswordStrength("Password123!");
        assertEquals(5, strength,
                "Una contraseña que cumple los 5 requisitos debe tener fortaleza 5");
    }
}