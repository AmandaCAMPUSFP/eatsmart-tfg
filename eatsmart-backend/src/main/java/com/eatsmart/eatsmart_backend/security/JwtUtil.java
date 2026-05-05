package com.eatsmart.eatsmart_backend.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Slf4j
@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration:900000}")
    private long expiration;

    @Value("${jwt.refresh-expiration:604800000}")
    private long refreshExpiration;

    /**
     * Obtiene la clave secreta para firmar tokens
     * Valida longitud mínima del secret
     */
    private SecretKey getSigningKey() {
        if (secret == null || secret.length() < 32) {
            throw new IllegalArgumentException("JWT secret debe tener mínimo 32 caracteres");
        }
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Genera un token JWT de acceso (15 minutos)
     */
    public String generarToken(String email, Long idUsuario) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", idUsuario);
        claims.put("type", "access");
        return crearToken(claims, email, expiration);
    }

    /**
     * Genera un refresh token (7 días)
     */
    public String generarRefreshToken(String email, Long idUsuario) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", idUsuario);
        claims.put("type", "refresh");
        return crearToken(claims, email, refreshExpiration);
    }

    /**
     * Crea el token JWT
     */
    private String crearToken(Map<String, Object> claims, String subject, long expirationTime) {
        Date ahora = new Date();
        Date fechaExpiracion = new Date(ahora.getTime() + expirationTime);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(ahora)
                .setExpiration(fechaExpiracion)
                .signWith(getSigningKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    /**
     * Extrae el email del token
     */
    public String extraerEmail(String token) {
        return extraerClaim(token, Claims::getSubject);
    }

    /**
     * Extrae el ID del usuario del token
     */
    public Long extraerIdUsuario(String token) {
        try {
            return extraerClaim(token, claims -> claims.get("userId", Long.class));
        } catch (Exception e) {
            log.warn("No se pudo extraer userId del token", e);
            return null;
        }
    }

    /**
     * Extrae el tipo de token (access o refresh)
     */
    public String extraerTipo(String token) {
        return extraerClaim(token, claims -> (String) claims.get("type"));
    }

    /**
     * Extrae la fecha de expiración
     */
    public Date extraerFechaExpiracion(String token) {
        return extraerClaim(token, Claims::getExpiration);
    }

    /**
     * Extrae un claim específico
     */
    public <T> T extraerClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extraerTodosClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Extrae todos los claims del token
     */
    private Claims extraerTodosClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (io.jsonwebtoken.security.SignatureException e) {
            log.error("Token signature inválida");
            throw new RuntimeException("Token signature inválida", e);
        } catch (io.jsonwebtoken.ExpiredJwtException e) {
            log.warn("Token expirado");
            throw new RuntimeException("Token expirado", e);
        } catch (Exception e) {
            log.error("Token inválido: {}", e.getMessage());
            throw new RuntimeException("Token inválido", e);
        }
    }

    /**
     * Comprueba si el token ha expirado
     */
    private Boolean estaExpirado(String token) {
        try {
            return extraerFechaExpiracion(token).before(new Date());
        } catch (Exception e) {
            return true;
        }
    }

    /**
     * Valida el token completo (email + ID + no expirado)
     */
    public Boolean validarToken(String token, String email, Long idUsuario) {
        try {
            final String emailDelToken = extraerEmail(token);
            final Long idDelToken = extraerIdUsuario(token);
            return (emailDelToken.equals(email) &&
                    idDelToken.equals(idUsuario) &&
                    !estaExpirado(token));
        } catch (Exception e) {
            log.warn("Error al validar token: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Valida solo la estructura del token
     */
    public Boolean esTokenValido(String token) {
        try {
            if (token == null || token.isEmpty()) {
                return false;
            }
            Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token);
            return !estaExpirado(token);
        } catch (io.jsonwebtoken.ExpiredJwtException e) {
            log.debug("Token expirado");
            return false;
        } catch (Exception e) {
            log.debug("Token inválido: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Validar si es un refresh token
     */
    public Boolean esRefreshToken(String token) {
        try {
            String type = extraerClaim(token, claims -> (String) claims.get("type"));
            return "refresh".equals(type);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Validar si es un access token
     */
    public Boolean esAccessToken(String token) {
        try {
            String type = extraerClaim(token, claims -> (String) claims.get("type"));
            return "access".equals(type);
        } catch (Exception e) {
            return false;
        }
    }
}