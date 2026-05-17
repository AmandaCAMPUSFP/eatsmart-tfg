package com.eatsmart.eatsmart_backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Entidad que representa un refresh token invalidado (blacklist).
 *
 * Cuando un usuario hace logout, su refresh token se registra aquí.
 * En el endpoint /refresh se comprueba que el token NO esté en esta tabla
 * antes de generar un nuevo access token.
 *
 * Mitiga OWASP A07:2021 - Identification and Authentication Failures:
 * evita que un refresh token robado siga siendo válido tras el logout.
 */
@Entity
@Table(name = "REFRESH_TOKEN_INVALIDADO")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RefreshTokenInvalidado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * Hash del refresh token (no se guarda el token en claro por seguridad).
     */
    @Column(name = "token_hash", nullable = false, length = 255)
    private String tokenHash;

    /**
     * Momento en que se invalidó (logout).
     */
    @Column(name = "fecha_invalidacion", nullable = false)
    private LocalDateTime fechaInvalidacion;

    /**
     * Momento en que el token expiraría de forma natural.
     * Permite al job de limpieza borrar registros que ya no hacen falta.
     */
    @Column(name = "fecha_expiracion", nullable = false)
    private LocalDateTime fechaExpiracion;
}