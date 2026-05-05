package com.eatsmart.eatsmart_backend.service;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;
import io.github.bucket4j.Refill;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Servicio de rate limiting para prevenir ataques de fuerza bruta
 * Máximo 5 intentos de login por IP en 5 minutos
 */
@Slf4j
@Service
public class RateLimitingService {

    private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();

    /**
     * Obtiene o crea un bucket para una IP
     * Límite: 5 intentos por 5 minutos
     */
    private Bucket resolveBucket(String ip) {
        return buckets.computeIfAbsent(ip, k -> createNewBucket());
    }

    /**
     * Crea un nuevo bucket con límite de 5 intentos por 5 minutos
     */
    private Bucket createNewBucket() {
        Bandwidth limit = Bandwidth.classic(5, Refill.intervally(5, Duration.ofMinutes(5)));
        return Bucket4j.builder()
                .addLimit(limit)
                .build();
    }

    /**
     * Verifica si la IP puede hacer una petición
     * @param ip dirección IP del cliente
     * @return true si puede, false si ha excedido el límite
     */
    public boolean allowRequest(String ip) {
        Bucket bucket = resolveBucket(ip);
        boolean allowed = bucket.tryConsume(1);

        if (!allowed) {
            log.warn("Rate limit alcanzado para IP: {}", ip);
        }

        return allowed;
    }

    /**
     * Obtiene los intentos restantes para una IP
     */
    public long getRemainingTokens(String ip) {
        Bucket bucket = resolveBucket(ip);
        return bucket.getAvailableTokens();
    }

    /**
     * Limpia el bucket de una IP (por ejemplo, después de login exitoso)
     */
    public void resetBucket(String ip) {
        buckets.remove(ip);
        log.debug("Bucket reiniciado para IP: {}", ip);
    }
}