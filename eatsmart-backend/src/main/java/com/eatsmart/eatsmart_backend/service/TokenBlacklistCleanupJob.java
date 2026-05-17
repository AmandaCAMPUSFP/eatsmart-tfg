package com.eatsmart.eatsmart_backend.service;

import com.eatsmart.eatsmart_backend.repository.RefreshTokenInvalidadoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * Job programado que limpia la blacklist de refresh tokens.
 *
 * Cada día elimina los tokens cuya fecha de expiración ya pasó:
 * un token ya expirado no necesita estar en la blacklist porque
 * de todas formas sería rechazado por estar caducado.
 *
 * Esto evita que la tabla REFRESH_TOKEN_INVALIDADO crezca indefinidamente.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class TokenBlacklistCleanupJob {

    private final RefreshTokenInvalidadoRepository repository;

    /**
     * Se ejecuta una vez al día (cada 24 horas).
     * fixedRate = 86400000 ms = 24 horas.
     */
    @Scheduled(fixedRate = 86400000)
    public void limpiarTokensExpirados() {
        try {
            int eliminados = repository.eliminarExpirados(LocalDateTime.now());
            if (eliminados > 0) {
                log.info("Job de limpieza: {} refresh tokens expirados eliminados de la blacklist", eliminados);
            }
        } catch (Exception e) {
            log.error("Error en el job de limpieza de blacklist: {}", e.getMessage());
        }
    }
}