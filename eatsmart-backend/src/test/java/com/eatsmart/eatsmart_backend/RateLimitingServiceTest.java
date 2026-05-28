package com.eatsmart.eatsmart_backend;

import com.eatsmart.eatsmart_backend.service.RateLimitingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Tests del servicio de rate limiting")
class RateLimitingServiceTest {

    private RateLimitingService rateLimitingService;

    @BeforeEach
    void setUp() {
        rateLimitingService = new RateLimitingService();
    }

    @Test
    @DisplayName("Una IP nueva tiene los 5 intentos disponibles")
    void ipNueva_tieneCincoIntentos() {
        long restantes = rateLimitingService.getRemainingTokens("192.168.1.1");

        assertEquals(5L, restantes);
    }

    @Test
    @DisplayName("Los primeros 5 intentos deben permitirse")
    void cincoIntentos_todosPermitidos() {
        String ip = "192.168.1.2";

        for (int i = 0; i < 5; i++) {
            assertTrue(rateLimitingService.allowRequest(ip),
                    "El intento número " + (i + 1) + " debería permitirse");
        }
    }

    @Test
    @DisplayName("El sexto intento debe bloquearse")
    void sextoIntento_debeBloquearse() {
        String ip = "192.168.1.3";

        // Consumo los 5 intentos permitidos
        for (int i = 0; i < 5; i++) {
            rateLimitingService.allowRequest(ip);
        }

        // El sexto debe fallar
        assertFalse(rateLimitingService.allowRequest(ip));
    }

    @Test
    @DisplayName("Las IPs distintas tienen contadores independientes")
    void ipsDistintas_contadoresIndependientes() {
        String ipA = "192.168.1.10";
        String ipB = "192.168.1.20";

        // Agoto los intentos de A
        for (int i = 0; i < 5; i++) {
            rateLimitingService.allowRequest(ipA);
        }

        // B sigue teniendo sus 5 intentos
        assertEquals(5L, rateLimitingService.getRemainingTokens(ipB));
        assertTrue(rateLimitingService.allowRequest(ipB));
    }

    @Test
    @DisplayName("Reiniciar el bucket devuelve los intentos a una IP")
    void resetBucket_devuelveLosIntentos() {
        String ip = "192.168.1.50";

        // Agoto los intentos
        for (int i = 0; i < 5; i++) {
            rateLimitingService.allowRequest(ip);
        }

        // Reseteo
        rateLimitingService.resetBucket(ip);

        // Vuelve a tener 5
        assertEquals(5L, rateLimitingService.getRemainingTokens(ip));
        assertTrue(rateLimitingService.allowRequest(ip));
    }
}