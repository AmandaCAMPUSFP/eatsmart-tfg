package com.eatsmart.eatsmart_backend.service;

import com.eatsmart.eatsmart_backend.entity.Usuario;
import com.eatsmart.eatsmart_backend.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

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
     */
    public Usuario registrar(Usuario usuario) {
        // Verificar que el email no exista
        if (usuarioRepository.findByEmail(usuario.getEmail()).isPresent()) {
            throw new RuntimeException("El email ya está registrado");
        }

        // Encriptar contraseña
        usuario.setContrasenaHash(passwordEncoder.encode(usuario.getContrasenaHash()));
        usuario.setFechaCreacion(LocalDateTime.now());
        usuario.setActivo("S");

        return usuarioRepository.save(usuario);
    }

    /**
     * Autentica un usuario validando email y contraseña
     */
    public Usuario autenticar(String email, String contrasena) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Email o contraseña incorrectos"));

        // Verificar contraseña
        if (!passwordEncoder.matches(contrasena, usuario.getContrasenaHash())) {
            throw new RuntimeException("Email o contraseña incorrectos");
        }

        return usuario;
    }

    public Usuario crear(Usuario usuario) {
        usuario.setFechaCreacion(LocalDateTime.now());
        usuario.setActivo("S");
        // Encriptar contraseña si no viene ya encriptada
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
                    return usuarioRepository.save(usuario);
                })
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    public void eliminar(Long id) {
        usuarioRepository.deleteById(id);
    }
}