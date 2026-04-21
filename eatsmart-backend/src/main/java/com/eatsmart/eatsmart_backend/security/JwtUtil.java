package com.eatsmart.eatsmart_backend.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtil {

    @Value("${jwt.secret:eatsmart_secret_key_2026_super_segura_cambiar_en_produccion}")
    private String secret;

    @Value("${jwt.expiration:86400000}") // 24 horas por defecto
    private long expiration;

    /**
     * Obtiene la clave secreta para firmar tokens
     */
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    /**
     * Genera un token JWT para un usuario
     * @param email del usuario
     * @return token JWT
     */
    public String generarToken(String email) {
        Map<String, Object> claims = new HashMap<>();
        return crearToken(claims, email);
    }

    /**
     * Crea el token JWT
     */
    private String crearToken(Map<String, Object> claims, String subject) {
        Date ahora = new Date();
        Date fechaExpiracion = new Date(ahora.getTime() + expiration);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(ahora)
                .setExpiration(fechaExpiracion)
                .signWith(getSigningKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    /**
     * Extrae el email (subject) del token
     */
    public String extraerEmail(String token) {
        return extraerClaim(token, Claims::getSubject);
    }

    /**
     * Extrae la fecha de expiración del token
     */
    public Date extraerFechaExpiracion(String token) {
        return extraerClaim(token, Claims::getExpiration);
    }

    /**
     * Extrae un claim específico del token
     */
    public <T> T extraerClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extraerTodosClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Extrae todos los claims del token
     */
    private Claims extraerTodosClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Comprueba si el token ha expirado
     */
    private Boolean estaExpirado(String token) {
        return extraerFechaExpiracion(token).before(new Date());
    }

    /**
     * Valida el token
     * @param token a validar
     * @param email del usuario esperado
     * @return true si es válido
     */
    public Boolean validarToken(String token, String email) {
        final String emailDelToken = extraerEmail(token);
        return (emailDelToken.equals(email) && !estaExpirado(token));
    }

    /**
     * Valida solo la estructura del token (sin comprobar email)
     */
    public Boolean esTokenValido(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token);
            return !estaExpirado(token);
        } catch (Exception e) {
            return false;
        }
    }
}