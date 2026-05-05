package com.eatsmart.eatsmart_backend.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Filtro JWT que valida tokens en cada petición
 * Se ejecuta una sola vez por request (OncePerRequestFilter)
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        try {
            String token = extraerToken(request);

            if (token != null && jwtUtil.esTokenValido(token)) {
                // Validar que es un access token
                if (!jwtUtil.esAccessToken(token)) {
                    log.warn("Token no es de tipo access");
                    filterChain.doFilter(request, response);
                    return;
                }

                String email = jwtUtil.extraerEmail(token);
                Long idUsuario = jwtUtil.extraerIdUsuario(token);

                log.debug("Token válido para usuario: {} (id: {})", email, idUsuario);

                UsernamePasswordAuthenticationToken auth =
                        new UsernamePasswordAuthenticationToken(email, null, new ArrayList<>());

                // Agregar ID de usuario en details
                auth.setDetails(idUsuario);

                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        } catch (Exception e) {
            log.error("Error procesando JWT: {}", e.getMessage());
        }

        filterChain.doFilter(request, response);
    }

    /**
     * Extrae el token del header Authorization
     * Formato esperado: "Bearer <token>"
     */
    private String extraerToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }

        return null;
    }
}