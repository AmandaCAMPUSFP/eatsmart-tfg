package com.eatsmart.eatsmart_backend.security;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

/**
 * Configuración de seguridad de la aplicación.
 * - Autenticación stateless con JWT (sin sesiones de servidor).
 * - Hashing de contraseñas con BCrypt.
 * - CORS configurable mediante variable de entorno (CORS_ORIGINS).
 * - Cabeceras de seguridad HTTP (CSP, HSTS, X-Frame-Options, etc.).
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
     * Orígenes permitidos para CORS, configurables por variable de entorno.
     * Por defecto, el frontend Angular en desarrollo (localhost:4200).
     * En producción se define la variable de entorno CORS_ORIGINS
     * (admite varios separados por coma).
     */
    @Value("${app.cors.allowed-origins:http://localhost:4200}")
    private String allowedOrigins;

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

                // ===== CABECERAS DE SEGURIDAD (OWASP A05) =====
                .headers(headers -> headers
                        // Evita clickjacking: la app no se puede embeber en iframes.
                        .frameOptions(frame -> frame.deny())
                        // Evita MIME-sniffing.
                        .contentTypeOptions(Customizer.withDefaults())
                        // Fuerza HTTPS durante 1 año (HSTS).
                        .httpStrictTransportSecurity(hsts -> hsts
                                .includeSubDomains(true)
                                .maxAgeInSeconds(31536000))
                        // Content Security Policy: defensa contra XSS.
                        .contentSecurityPolicy(csp -> csp.policyDirectives(
                                "default-src 'self'; " +
                                        "script-src 'self'; " +
                                        "style-src 'self' 'unsafe-inline'; " +
                                        "img-src 'self' data:; " +
                                        "font-src 'self'; " +
                                        "connect-src 'self'; " +
                                        "frame-ancestors 'none'"))
                        // Controla qué información de referencia se envía.
                        .referrerPolicy(ref -> ref.policy(
                                ReferrerPolicyHeaderWriter.ReferrerPolicy.STRICT_ORIGIN_WHEN_CROSS_ORIGIN))
                )

                .authorizeHttpRequests(authz -> authz
                        // ===== ENDPOINTS PÚBLICOS =====
                        // Solo registro y login son completamente públicos
                        .requestMatchers("/api/auth/registro", "/api/auth/login", "/api/auth/logout", "/api/auth/refresh").permitAll()
                        // Swagger / OpenAPI: documentación pública de la API
                        .requestMatchers("/swagger-ui/**", "/swagger-ui.html", "/v3/api-docs/**").permitAll()

                        // Catálogo de alimentos: solo GET es público (consultar)
                        .requestMatchers(HttpMethod.GET, "/api/alimentos/**").permitAll()
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
     * Configuración CORS. Los orígenes permitidos se leen de la variable
     * de entorno CORS_ORIGINS (o el valor por defecto localhost:4200).
     * Admite varios orígenes separados por coma.
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList(allowedOrigins.split(",")));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}