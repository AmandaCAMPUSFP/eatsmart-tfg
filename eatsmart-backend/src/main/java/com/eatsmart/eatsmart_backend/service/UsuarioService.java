package com.eatsmart.eatsmart_backend.service;

import com.eatsmart.eatsmart_backend.entity.Usuario;
import com.eatsmart.eatsmart_backend.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final PasswordValidationService passwordValidationService;

    public List<Usuario> obtenerTodos() {
        return usuarioRepository.findAll();
    }

    public Optional<Usuario> obtenerPorId(Long id) {
        return usuarioRepository.findById(id);
    }

    public Optional<Usuario> obtenerPorEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    /**
     * Registra un nuevo usuario con contraseña encriptada
     * Validar fortaleza de contraseña
     */
    public Usuario registrar(Usuario usuario) {
        // Validar que el email no exista
        if (usuarioRepository.findByEmail(usuario.getEmail()).isPresent()) {
            log.warn("Intento de registro con email duplicado: {}", usuario.getEmail());
            throw new RuntimeException("El email ya está registrado");
        }

        // Validar fortaleza de contraseña
        try {
            passwordValidationService.validarContraseña(usuario.getContrasenaHash());
        } catch (IllegalArgumentException e) {
            log.warn("Contraseña débil en registro: {}", e.getMessage());
            throw e;
        }

        // Encriptar contraseña
        usuario.setContrasenaHash(passwordEncoder.encode(usuario.getContrasenaHash()));
        usuario.setFechaCreacion(LocalDateTime.now());
        usuario.setActivo("S");

        Usuario registrado = usuarioRepository.save(usuario);
        log.info("Usuario registrado exitosamente: {}", usuario.getEmail());
        return registrado;
    }

    /**
     * Autentica un usuario
     * Logging de intentos fallidos
     */
    public Usuario autenticar(String email, String contrasena) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> {
                    log.warn("Intento de login con email no registrado: {}", email);
                    return new RuntimeException("Email o contraseña incorrectos");
                });

        if (!passwordEncoder.matches(contrasena, usuario.getContrasenaHash())) {
            log.warn("Intento de login fallido para usuario: {}", email);
            throw new RuntimeException("Email o contraseña incorrectos");
        }

        log.info("Login exitoso para usuario: {}", email);
        return usuario;
    }

    public Usuario crear(Usuario usuario) {
        usuario.setFechaCreacion(LocalDateTime.now());
        usuario.setActivo("S");
        if (!usuario.getContrasenaHash().startsWith("$2a$") && !usuario.getContrasenaHash().startsWith("$2b$")) {
            usuario.setContrasenaHash(passwordEncoder.encode(usuario.getContrasenaHash()));
        }
        return usuarioRepository.save(usuario);
    }

    public Usuario actualizar(Long id, Usuario usuarioActualizado) {
        return usuarioRepository.findById(id)
                .map(usuario -> {
                    usuario.setEmail(usuarioActualizado.getEmail());
                    usuario.setActivo(usuarioActualizado.getActivo());
                    log.info("Usuario actualizado: {}", id);
                    return usuarioRepository.save(usuario);
                })
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    public void eliminar(Long id) {
        usuarioRepository.deleteById(id);
        log.info("Usuario eliminado: {}", id);
    }
}