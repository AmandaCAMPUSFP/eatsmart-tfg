package com.eatsmart.eatsmart_backend;

import com.eatsmart.eatsmart_backend.entity.RefreshTokenInvalidado;
import com.eatsmart.eatsmart_backend.repository.RefreshTokenInvalidadoRepository;
import com.eatsmart.eatsmart_backend.service.TokenBlacklistService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests del servicio de blacklist de refresh tokens")
class TokenBlacklistServiceTest {

    @Mock
    private RefreshTokenInvalidadoRepository repository;

    @InjectMocks
    private TokenBlacklistService blacklistService;

    private String tokenEjemplo;
    private LocalDateTime fechaExpiracion;

    @BeforeEach
    void setUp() {
        tokenEjemplo = "eyJhbGciOiJIUzUxMiJ9.token_de_prueba.firma";
        fechaExpiracion = LocalDateTime.now().plusDays(7);
    }

    @Test
    @DisplayName("Invalidar un token nuevo debe guardarlo con su hash en la BD")
    void invalidarToken_tokenNuevo_seGuardaConHash() {
        when(repository.existsByTokenHash(anyString())).thenReturn(false);

        blacklistService.invalidarToken(tokenEjemplo, fechaExpiracion);

        ArgumentCaptor<RefreshTokenInvalidado> captor =
                ArgumentCaptor.forClass(RefreshTokenInvalidado.class);
        verify(repository).save(captor.capture());

        RefreshTokenInvalidado guardado = captor.getValue();
        assertNotNull(guardado.getTokenHash());
        // Verificamos que NO guarda el token en claro
        assertNotEquals(tokenEjemplo, guardado.getTokenHash());
        // El hash SHA-256 tiene 64 caracteres en hexadecimal
        assertEquals(64, guardado.getTokenHash().length());
    }

    @Test
    @DisplayName("Invalidar un token ya invalidado no debe duplicar el registro")
    void invalidarToken_tokenYaInvalidado_noSeDuplica() {
        when(repository.existsByTokenHash(anyString())).thenReturn(true);

        blacklistService.invalidarToken(tokenEjemplo, fechaExpiracion);

        verify(repository, never()).save(any(RefreshTokenInvalidado.class));
    }

    @Test
    @DisplayName("Comprobar token invalidado debe devolver true si está en blacklist")
    void estaInvalidado_tokenEnBlacklist_devuelveTrue() {
        when(repository.existsByTokenHash(anyString())).thenReturn(true);

        boolean resultado = blacklistService.estaInvalidado(tokenEjemplo);

        assertTrue(resultado);
    }

    @Test
    @DisplayName("Comprobar token NO invalidado debe devolver false")
    void estaInvalidado_tokenNoEnBlacklist_devuelveFalse() {
        when(repository.existsByTokenHash(anyString())).thenReturn(false);

        boolean resultado = blacklistService.estaInvalidado(tokenEjemplo);

        assertFalse(resultado);
    }

    @Test
    @DisplayName("Tokens distintos generan hashes distintos (consistencia SHA-256)")
    void hash_tokensDistintos_generanHashesDistintos() {
        when(repository.existsByTokenHash(anyString())).thenReturn(false);

        blacklistService.invalidarToken("token_A", fechaExpiracion);
        blacklistService.invalidarToken("token_B", fechaExpiracion);

        ArgumentCaptor<RefreshTokenInvalidado> captor =
                ArgumentCaptor.forClass(RefreshTokenInvalidado.class);
        verify(repository, times(2)).save(captor.capture());

        String hashA = captor.getAllValues().get(0).getTokenHash();
        String hashB = captor.getAllValues().get(1).getTokenHash();
        assertNotEquals(hashA, hashB);
    }
}