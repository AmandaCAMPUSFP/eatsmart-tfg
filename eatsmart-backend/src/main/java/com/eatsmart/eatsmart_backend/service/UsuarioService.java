package com.eatsmart.eatsmart_backend.service;

import com.eatsmart.eatsmart_backend.dto.UsuarioDTO;
import com.eatsmart.eatsmart_backend.entity.Usuario;
import com.eatsmart.eatsmart_backend.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Servicio que gestiona la lógica de negocio relacionada con los usuarios.
 * Aplica validaciones de registro, cifrado de contraseñas con BCrypt
 * y las transformaciones necesarias entre entidades y DTOs.
 */
@Service
@RequiredArgsConstructor
public class UsuarioService {

    private static final String ESTADO_ACTIVO = "S";

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Recupera el listado completo de usuarios registrados.
     *
     * @return lista de {@link UsuarioDTO} con todos los usuarios
     */
    public List<UsuarioDTO> getAll() {
        return usuarioRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Busca un usuario por su identificador único.
     *
     * @param id identificador del usuario
     * @return {@link Optional} con el {@link UsuarioDTO} si existe, vacío en caso contrario
     */
    public Optional<UsuarioDTO> getById(Long id) {
        return usuarioRepository.findById(id).map(this::toDTO);
    }

    /**
     * Registra un nuevo usuario en el sistema.
     * Valida que el email no esté duplicado y aplica BCrypt a la contraseña
     * antes de persistirla.
     *
     * @param dto datos del usuario a registrar
     * @return {@link UsuarioDTO} con los datos guardados, incluyendo el identificador generado
     * @throws IllegalArgumentException si el email ya existe o los datos son inválidos
     */
    public UsuarioDTO save(UsuarioDTO dto) {
        if (dto.getEmail() == null || dto.getEmail().isBlank()) {
            throw new IllegalArgumentException("El email es obligatorio.");
        }
        if (dto.getContrasena() == null || dto.getContrasena().isBlank()) {
            throw new IllegalArgumentException("La contraseña es obligatoria.");
        }
        if (usuarioRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Ya existe un usuario registrado con el email: " + dto.getEmail());
        }

        Usuario usuario = new Usuario();
        usuario.setEmail(dto.getEmail());
        usuario.setContrasenaHash(passwordEncoder.encode(dto.getContrasena()));
        usuario.setFechaCreacion(LocalDateTime.now());
        usuario.setActivo(ESTADO_ACTIVO);

        return toDTO(usuarioRepository.save(usuario));
    }

    // ── Métodos auxiliares de conversión ─────────────────────────────────────

    private UsuarioDTO toDTO(Usuario usuario) {
        return new UsuarioDTO(usuario.getIdUsuario(), usuario.getEmail(), null);
    }
}
