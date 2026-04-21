package com.eatsmart.eatsmart_backend.controller;

import com.eatsmart.eatsmart_backend.dto.AuthRequest;
import com.eatsmart.eatsmart_backend.dto.AuthResponse;
import com.eatsmart.eatsmart_backend.entity.Usuario;
import com.eatsmart.eatsmart_backend.security.JwtUtil;
import com.eatsmart.eatsmart_backend.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UsuarioService usuarioService;
    private final JwtUtil jwtUtil;

    /**
     * Endpoint para registrar un nuevo usuario
     * POST /api/auth/registro
     */
    @PostMapping("/registro")
    public ResponseEntity<AuthResponse> registro(@Valid @RequestBody AuthRequest authRequest) {
        try {
            Usuario usuario = new Usuario();
            usuario.setEmail(authRequest.getEmail());
            usuario.setContrasenaHash(authRequest.getContrasena());

            Usuario usuarioRegistrado = usuarioService.registrar(usuario);
            String token = jwtUtil.generarToken(usuarioRegistrado.getEmail());

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
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new AuthResponse(e.getMessage(), false)
            );
        }
    }

    /**
     * Endpoint para iniciar sesión
     * POST /api/auth/login
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthRequest authRequest) {
        try {
            Usuario usuarioAutenticado = usuarioService.autenticar(
                    authRequest.getEmail(),
                    authRequest.getContrasena()
            );

            String token = jwtUtil.generarToken(usuarioAutenticado.getEmail());

            return ResponseEntity.ok(
                    new AuthResponse(
                            "Inicio de sesión exitoso",
                            token,
                            usuarioAutenticado.getIdUsuario(),
                            usuarioAutenticado.getEmail(),
                            true
                    )
            );
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                    new AuthResponse(e.getMessage(), false)
            );
        }
    }

    /**
     * Endpoint para validar un token (test)
     * GET /api/auth/validar?token=xxx
     */
    @GetMapping("/validar")
    public ResponseEntity<AuthResponse> validarToken(@RequestParam String token) {
        boolean esValido = jwtUtil.esTokenValido(token);

        if (esValido) {
            String email = jwtUtil.extraerEmail(token);
            return ResponseEntity.ok(
                    new AuthResponse("Token válido", email + " autenticado", 0L, email, true)
            );
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                    new AuthResponse("Token inválido o expirado", false)
            );
        }
    }
}