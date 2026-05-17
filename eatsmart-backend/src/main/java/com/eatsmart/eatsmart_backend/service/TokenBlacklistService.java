package com.eatsmart.eatsmart_backend.service;

import com.eatsmart.eatsmart_backend.entity.RefreshTokenInvalidado;
import com.eatsmart.eatsmart_backend.repository.RefreshTokenInvalidadoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.LocalDateTime;
import java.util.HexFormat;

/**
 * Servicio para la gestión de la blacklist de refresh tokens.
 *
 * Mitiga OWASP A07:2021 - Identification and Authentication Failures.
 * Al hacer logout, el refresh token se registra (por su hash) en la blacklist.
 * Al intentar refrescar, se comprueba que el token NO esté en la blacklist.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TokenBlacklistService {

    private final RefreshTokenInvalidadoRepository repository;

    /**
     * Invalida un refresh token añadiéndolo a la blacklist.
     *
     * @param refreshToken el token a invalidar
     * @param fechaExpiracion cuándo expiraría el token de forma natural
     */
    public void invalidarToken(String refreshToken, LocalDateTime fechaExpiracion) {
        String hash = hashToken(refreshToken);

        // Si ya estaba invalidado, no duplicamos
        if (repository.existsByTokenHash(hash)) {
            return;
        }

        RefreshTokenInvalidado registro = new RefreshTokenInvalidado();
        registro.setTokenHash(hash);
        registro.setFechaInvalidacion(LocalDateTime.now());
        registro.setFechaExpiracion(fechaExpiracion);

        repository.save(registro);
        log.info("Refresh token invalidado y añadido a la blacklist");
    }

    /**
     * Comprueba si un refresh token está en la blacklist (invalidado).
     */
    public boolean estaInvalidado(String refreshToken) {
        String hash = hashToken(refreshToken);
        return repository.existsByTokenHash(hash);
    }

    /**
     * Calcula el hash SHA-256 del token.
     * No guardamos el token en claro en la base de datos por seguridad.
     */
    private String hashToken(String token) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(token.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(hashBytes);
        } catch (Exception e) {
            log.error("Error calculando hash del token: {}", e.getMessage());
            // Fallback: si fallara el hash, usamos el token tal cual
            // (no debería ocurrir, SHA-256 siempre está disponible en la JVM)
            return token;
        }
    }
}