package com.eatsmart.eatsmart_backend.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

/**
 * Configuración de seguridad de la aplicación.
 * - Autenticación stateless con JWT (sin sesiones de servidor).
 * - Hashing de contraseñas con BCrypt.
 * - CORS configurado para permitir el origen del frontend Angular.
 * - Endpoints públicos limitados a registro, login y consulta de catálogos (GET).
 *
 * Mitiga: OWASP A01 (Broken Access Control), A05 (Security Misconfiguration).
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    /**
     * Bean de PasswordEncoder para encriptar contraseñas con BCrypt.
     * Mitiga OWASP A02 (Cryptographic Failures) - nunca se almacenan contraseñas en claro.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Cadena de filtros de seguridad HTTP.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // CSRF deshabilitado: usamos JWT en lugar de cookies de sesión.
                .csrf(csrf -> csrf.disable())

                // CORS habilitado para que el frontend Angular pueda llamar a la API.
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                // Sin sesiones HTTP: cada petición lleva su propio token JWT.
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .authorizeHttpRequests(authz -> authz
                        // ===== ENDPOINTS PÚBLICOS =====
                        // Solo registro y login son completamente públicos
                        .requestMatchers("/api/auth/registro", "/api/auth/login").permitAll()

                        // Catálogo de alimentos: solo GET es público (consultar)
                        .requestMatchers(HttpMethod.GET, "/api/alimentos/**").permitAll()
                        // Crear/modificar/borrar alimentos requiere autenticación
                        .requestMatchers(HttpMethod.POST, "/api/alimentos/**").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/api/alimentos/**").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/api/alimentos/**").authenticated()

                        // Catálogo de recetas: solo GET es público
                        .requestMatchers(HttpMethod.GET, "/api/recetas/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/recetas/**").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/api/recetas/**").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/api/recetas/**").authenticated()

                        // ===== ENDPOINTS PRIVADOS =====
                        // Datos personales: SIEMPRE autenticado
                        .requestMatchers("/api/usuarios/**").authenticated()
                        .requestMatchers("/api/perfiles-nutricionales/**").authenticated()
                        .requestMatchers("/api/comidas/**").authenticated()

                        // Cualquier otra petición requiere autenticación
                        .anyRequest().authenticated()
                )

                // Filtro JWT antes del filtro de autenticación estándar
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * Configuración CORS para permitir peticiones desde el frontend Angular.
     * En desarrollo: localhost:4200 (puerto por defecto de ng serve).
     * En producción: añadir el dominio real del frontend desplegado.
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // Origen del frontend Angular en desarrollo
        configuration.setAllowedOrigins(Arrays.asList(
                "http://localhost:4200"
        ));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}