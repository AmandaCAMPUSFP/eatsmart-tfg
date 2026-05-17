package com.eatsmart.eatsmart_backend.controller;

import com.eatsmart.eatsmart_backend.dto.AuthRequest;
import com.eatsmart.eatsmart_backend.dto.AuthResponse;
import com.eatsmart.eatsmart_backend.entity.Usuario;
import com.eatsmart.eatsmart_backend.security.JwtUtil;
import com.eatsmart.eatsmart_backend.service.RateLimitingService;
import com.eatsmart.eatsmart_backend.service.UsuarioService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UsuarioService usuarioService;
    private final JwtUtil jwtUtil;
    private final RateLimitingService rateLimitingService;

    /**
     * Obtener IP del cliente (considera X-Forwarded-For)
     */
    private String obtenerIPCliente(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0];
        }
        return request.getRemoteAddr();
    }

    /**
     * Extrae el token del header Authorization (formato "Bearer <token>").
     * Centralizado para reutilizar en validar y refresh.
     */
    private String extraerTokenDelHeader(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }

    /**
     * Registrar nuevo usuario
     */
    @PostMapping("/registro")
    public ResponseEntity<AuthResponse> registro(
            @Valid @RequestBody AuthRequest authRequest,
            HttpServletRequest request) {
        try {
            String ip = obtenerIPCliente(request);

            // Rate limiting para registro
            if (!rateLimitingService.allowRequest(ip + ":registro")) {
                log.warn("Rate limit alcanzado para registro desde IP: {}", ip);
                return ResponseEntity.status(429)
                        .body(new AuthResponse("Demasiados intentos. Intenta más tarde", false));
            }

            Usuario usuario = new Usuario();
            usuario.setEmail(authRequest.getEmail());
            usuario.setContrasenaHash(authRequest.getContrasena());

            Usuario usuarioRegistrado = usuarioService.registrar(usuario);
            String token = jwtUtil.generarToken(usuarioRegistrado.getEmail(), usuarioRegistrado.getIdUsuario());

            log.info("Usuario registrado: {}", usuarioRegistrado.getEmail());

            return ResponseEntity.status(HttpStatus.CREATED).body(
                    new AuthResponse(
                            "Usuario registrado exitosamente",
                            token,
                            usuarioRegistrado.getIdUsuario(),
                            usuarioRegistrado.getEmail(),
                            true
                    )
            );
        } catch (RuntimeException e) {
            log.error("Error en registro: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new AuthResponse(e.getMessage(), false)
            );
        }
    }

    /**
     * Login de usuario
     * Rate limiting, refresh token
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @Valid @RequestBody AuthRequest authRequest,
            HttpServletRequest request) {
        try {
            String ip = obtenerIPCliente(request);

            // Rate limiting - Máximo 5 intentos por 5 minutos
            if (!rateLimitingService.allowRequest(ip)) {
                log.warn("Rate limit alcanzado para login desde IP: {}", ip);
                return ResponseEntity.status(429)
                        .body(new AuthResponse(
                                "Demasiados intentos. Intenta de nuevo en 5 minutos",
                                false
                        ));
            }

            Usuario usuarioAutenticado = usuarioService.autenticar(
                    authRequest.getEmail(),
                    authRequest.getContrasena()
            );

            String accessToken = jwtUtil.generarToken(
                    usuarioAutenticado.getEmail(),
                    usuarioAutenticado.getIdUsuario()
            );

            // Refresh token
            String refreshToken = jwtUtil.generarRefreshToken(
                    usuarioAutenticado.getEmail(),
                    usuarioAutenticado.getIdUsuario()
            );

            // Limpiar rate limiting tras login exitoso
            rateLimitingService.resetBucket(ip);

            log.info("Login exitoso: {} desde IP {}", usuarioAutenticado.getEmail(), ip);

            AuthResponse response = new AuthResponse();
            response.setMensaje("Inicio de sesión exitoso");
            response.setToken(accessToken);
            response.setIdUsuario(usuarioAutenticado.getIdUsuario());
            response.setEmail(usuarioAutenticado.getEmail());
            response.setExitoso(true);
            response.setRefreshToken(refreshToken);

            return ResponseEntity.ok(response);

        } catch (RuntimeException e) {
            log.warn("Login fallido: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                    new AuthResponse(e.getMessage(), false)
            );
        }
    }

    /**
     * Endpoint para refrescar token.
     * El refresh token se envía en el cuerpo de la petición (NO en query param),
     * para evitar que quede registrado en logs, historial o proxies (OWASP A07).
     */
    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(@RequestBody Map<String, String> body) {
        try {
            String refreshToken = body.get("refreshToken");

            if (refreshToken == null
                    || !jwtUtil.esTokenValido(refreshToken)
                    || !jwtUtil.esRefreshToken(refreshToken)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new AuthResponse("Refresh token inválido", false));
            }

            String email = jwtUtil.extraerEmail(refreshToken);
            Long idUsuario = jwtUtil.extraerIdUsuario(refreshToken);

            String nuevoAccessToken = jwtUtil.generarToken(email, idUsuario);

            log.info("Token refrescado para usuario: {}", email);

            AuthResponse response = new AuthResponse();
            response.setMensaje("Token refrescado");
            response.setToken(nuevoAccessToken);
            response.setIdUsuario(idUsuario);
            response.setEmail(email);
            response.setExitoso(true);
            response.setRefreshToken(refreshToken);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("Error refrescando token: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new AuthResponse("Error refrescando token", false));
        }
    }

    /**
     * Validar token (para debug).
     * El token se envía en el header Authorization (NO en query param),
     * evitando que quede registrado en logs, historial o proxies (OWASP A07).
     */
    @PostMapping("/validar")
    public ResponseEntity<AuthResponse> validarToken(HttpServletRequest request) {
        String token = extraerTokenDelHeader(request);

        if (token == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                    new AuthResponse("Token no proporcionado", false)
            );
        }

        boolean esValido = jwtUtil.esTokenValido(token);

        if (esValido) {
            String email = jwtUtil.extraerEmail(token);
            Long idUsuario = jwtUtil.extraerIdUsuario(token);
            return ResponseEntity.ok(
                    new AuthResponse("Token válido", email + " autenticado", idUsuario, email, true)
            );
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                    new AuthResponse("Token inválido o expirado", false)
            );
        }
    }
}