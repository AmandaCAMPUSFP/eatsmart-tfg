package com.eatsmart.eatsmart_backend.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

/**
 * Servicio de seguridad para validación de propiedad de recursos (ownership).
 *
 * Mitiga OWASP A01:2021 - Broken Access Control (IDOR).
 * Comprueba que el usuario autenticado solo pueda acceder a sus propios recursos.
 *
 * El ID del usuario autenticado se obtiene del objeto Authentication,
 * donde el JwtAuthenticationFilter lo almacena mediante auth.setDetails(idUsuario).
 */
@Service("securityService")
public class SecurityService {

    /**
     * Comprueba si el usuario autenticado es el propietario del recurso solicitado.
     *
     * @param resourceUserId el ID de usuario al que pertenece el recurso (viene del path)
     * @return true si el usuario autenticado coincide con el propietario del recurso
     */
    public boolean isOwner(Long resourceUserId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || auth.getDetails() == null) {
            return false;
        }

        Object details = auth.getDetails();
        if (!(details instanceof Long)) {
            return false;
        }

        Long currentUserId = (Long) details;
        return currentUserId.equals(resourceUserId);
    }
}