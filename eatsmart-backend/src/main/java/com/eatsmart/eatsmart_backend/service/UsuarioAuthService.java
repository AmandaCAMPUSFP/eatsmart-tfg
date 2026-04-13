package com.eatsmart.eatsmart_backend.service;

import com.eatsmart.eatsmart_backend.entity.Usuario;
import com.eatsmart.eatsmart_backend.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class UsuarioAuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Registra un nuevo usuario con contraseña hasheada
     */
    public Usuario registrar(String email, String contrasena) {
        // Validar que no exista
        if (usuarioRepository.findByEmail(email).isPresent()) {
            throw new RuntimeException("El email ya está registrado");
        }

        Usuario usuario = new Usuario();
        usuario.setEmail(email);
        usuario.setContrasenaHash(passwordEncoder.encode(contrasena)); // HASH con BCrypt
        usuario.setFechaCreacion(LocalDateTime.now());
        usuario.setActivo("S");

        return usuarioRepository.save(usuario);
    }

    /**
     * Valida las credenciales de un usuario
     */
    public boolean validarCredenciales(String email, String contrasena) {
        return usuarioRepository.findByEmail(email)
                .map(usuario -> passwordEncoder.matches(contrasena, usuario.getContrasenaHash()))
                .orElse(false);
    }

    /**
     * Obtiene un usuario por email
     */
    public Usuario obtenerPorEmail(String email) {
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    /**
     * Cambia la contraseña de un usuario
     */
    public void cambiarContrasena(Long idUsuario, String contrasenaActual, String contrasenaNueva) {
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Validar contraseña actual
        if (!passwordEncoder.matches(contrasenaActual, usuario.getContrasenaHash())) {
            throw new RuntimeException("Contraseña actual incorrecta");
        }

        // Actualizar con la nueva
        usuario.setContrasenaHash(passwordEncoder.encode(contrasenaNueva));
        usuarioRepository.save(usuario);
    }
}