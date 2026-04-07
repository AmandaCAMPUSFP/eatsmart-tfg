package com.eatsmart.eatsmart_backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configuración de seguridad de EatSmart.
 *
 * <p>En esta fase inicial se exponen los endpoints de la API de forma pública
 * para facilitar las pruebas con Postman. En futuras iteraciones se incorporará
 * la autenticación mediante tokens JWT (RNF-01) y el cifrado de contraseñas
 * con BCrypt quedará operativo de forma completa (RNF-02).</p>
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Cadena de filtros de seguridad.
     * <ul>
     *   <li>CSRF deshabilitado: esta API REST es de tipo sin estado (stateless) y utiliza
     *       tokens JWT en cabeceras HTTP, no cookies de sesión, por lo que la protección
     *       CSRF no es aplicable (los ataques CSRF requieren autenticación basada en cookies).</li>
     *   <li>Gestión de sesión sin estado (STATELESS), preparada para JWT.</li>
     *   <li>Permite acceso público a todos los endpoints de la API.</li>
     * </ul>
     *
     * @param http objeto de configuración de Spring Security
     * @return la cadena de filtros configurada
     * @throws Exception si se produce algún error de configuración
     */
    @Bean
    @SuppressWarnings("java:S4502") // CSRF deshabilitado intencionadamente: API REST stateless con JWT
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll()
                );
        return http.build();
    }

    /**
     * Bean del codificador de contraseñas BCrypt utilizado por {@code UsuarioService}.
     *
     * @return instancia de {@link BCryptPasswordEncoder}
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
