package com.eatsmart.eatsmart_backend;

import com.eatsmart.eatsmart_backend.security.SecurityService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests de la validación de propiedad (ownership) de recursos.
 *
 * Verifica el cumplimiento de OWASP A01:2021 - Broken Access Control:
 * un usuario autenticado solo puede acceder a sus propios recursos.
 */
class SecurityServiceTest {

    private final SecurityService securityService = new SecurityService();

    /**
     * Simula un usuario autenticado con el ID dado, igual que hace
     * JwtAuthenticationFilter (auth.setDetails(idUsuario)).
     */
    private void simularUsuarioAutenticado(Long idUsuario) {
        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken("usuario@test.com", null, new ArrayList<>());
        auth.setDetails(idUsuario);
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    @AfterEach
    void limpiarContexto() {
        SecurityContextHolder.clearContext();
    }

    @Test
    @DisplayName("El usuario SÍ es propietario de su propio recurso")
    void usuarioEsPropietarioDeSuRecurso() {
        // Usuario autenticado con ID 1
        simularUsuarioAutenticado(1L);

        // Intenta acceder a su propio recurso (ID 1)
        boolean esPropietario = securityService.isOwner(1L);

        assertTrue(esPropietario,
                "El usuario 1 debería poder acceder a su propio recurso");
    }

    @Test
    @DisplayName("El usuario NO es propietario del recurso de otro usuario (OWASP A01)")
    void usuarioNoPuedeAccederARecursoDeOtro() {
        // Usuario autenticado con ID 1
        simularUsuarioAutenticado(1L);

        // Intenta acceder al recurso del usuario 5 (NO es suyo)
        boolean esPropietario = securityService.isOwner(5L);

        assertFalse(esPropietario,
                "El usuario 1 NO debería poder acceder al recurso del usuario 5");
    }

    @Test
    @DisplayName("Sin autenticación, isOwner devuelve false")
    void sinAutenticacionDevuelveFalse() {
        // No simulamos ningún usuario (contexto vacío)
        SecurityContextHolder.clearContext();

        boolean esPropietario = securityService.isOwner(1L);

        assertFalse(esPropietario,
                "Sin autenticación no se debe permitir el acceso");
    }
}