package com.eatsmart.eatsmart_backend.controller;

import com.eatsmart.eatsmart_backend.dto.LoginRequest;
import com.eatsmart.eatsmart_backend.dto.LoginResponse;
import com.eatsmart.eatsmart_backend.dto.RegistroRequest;
import com.eatsmart.eatsmart_backend.entity.Usuario;
import com.eatsmart.eatsmart_backend.service.UsuarioAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UsuarioAuthService usuarioAuthService;

    /**
     * Registro de nuevo usuario
     */
    @PostMapping("/registrar")
    public ResponseEntity<Usuario> registrar(@RequestBody RegistroRequest request) {
        if (request.getEmail() == null || request.getEmail().isEmpty()) {
            throw new RuntimeException("El email es obligatorio");
        }
        if (request.getContrasena() == null || request.getContrasena().length() < 6) {
            throw new RuntimeException("La contraseña debe tener al menos 6 caracteres");
        }

        Usuario usuario = usuarioAuthService.registrar(request.getEmail(), request.getContrasena());
        return ResponseEntity.ok(usuario);
    }

    /**
     * Login de usuario
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        if (!usuarioAuthService.validarCredenciales(request.getEmail(), request.getContrasena())) {
            throw new RuntimeException("Email o contraseña incorrectos");
        }

        Usuario usuario = usuarioAuthService.obtenerPorEmail(request.getEmail());

        LoginResponse response = new LoginResponse();
        response.setIdUsuario(usuario.getIdUsuario());
        response.setEmail(usuario.getEmail());
        response.setMensaje("Login exitoso");

        return ResponseEntity.ok(response);
    }

    /**
     * Cambiar contraseña
     */
    @PostMapping("/cambiar-contrasena/{idUsuario}")
    public ResponseEntity<String> cambiarContrasena(
            @PathVariable Long idUsuario,
            @RequestParam String contrasenaActual,
            @RequestParam String contrasenaNueva) {

        usuarioAuthService.cambiarContrasena(idUsuario, contrasenaActual, contrasenaNueva);
        return ResponseEntity.ok("Contraseña actualizada correctamente");
    }
}