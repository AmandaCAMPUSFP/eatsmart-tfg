package com.eatsmart.eatsmart_backend.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

/**
 * Servicio para validar la fortaleza de contraseñas
 * Requisitos:
 * - Mínimo 8 caracteres
 * - Al menos 1 mayúscula
 * - Al menos 1 minúscula
 * - Al menos 1 dígito
 * - Al menos 1 carácter especial
 */
@Slf4j
@Service
public class PasswordValidationService {

    private static final Pattern UPPERCASE = Pattern.compile("[A-Z]");
    private static final Pattern LOWERCASE = Pattern.compile("[a-z]");
    private static final Pattern DIGITS = Pattern.compile("[0-9]");
    private static final Pattern SPECIAL = Pattern.compile("[!@#$%^&*()_+\\-=\\[\\]{};':\",./<>?]");

    /**
     * Valida la fortaleza de una contraseña
     */
    public void validarContraseña(String contrasena) {
        if (contrasena == null || contrasena.length() < 8) {
            throw new IllegalArgumentException("La contraseña debe tener mínimo 8 caracteres");
        }

        if (!UPPERCASE.matcher(contrasena).find()) {
            throw new IllegalArgumentException("La contraseña debe contener al menos 1 mayúscula");
        }

        if (!LOWERCASE.matcher(contrasena).find()) {
            throw new IllegalArgumentException("La contraseña debe contener al menos 1 minúscula");
        }

        if (!DIGITS.matcher(contrasena).find()) {
            throw new IllegalArgumentException("La contraseña debe contener al menos 1 dígito");
        }

        if (!SPECIAL.matcher(contrasena).find()) {
            throw new IllegalArgumentException("La contraseña debe contener al menos 1 carácter especial");
        }
    }

    /**
     * Obtiene la fortaleza de la contraseña
     */
    public int getPasswordStrength(String contrasena) {
        int strength = 0;

        if (contrasena != null && contrasena.length() >= 8) strength++;
        if (UPPERCASE.matcher(contrasena).find()) strength++;
        if (LOWERCASE.matcher(contrasena).find()) strength++;
        if (DIGITS.matcher(contrasena).find()) strength++;
        if (SPECIAL.matcher(contrasena).find()) strength++;

        return strength;
    }
}