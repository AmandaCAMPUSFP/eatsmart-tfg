package com.eatsmart.eatsmart_backend.service;

import com.eatsmart.eatsmart_backend.entity.PerfilNutricional;
import com.eatsmart.eatsmart_backend.entity.Usuario;
import com.eatsmart.eatsmart_backend.repository.PerfilNutricionalRepository;
import com.eatsmart.eatsmart_backend.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class PerfilNutricionalService {

    private final PerfilNutricionalRepository perfilRepository;
    private final UsuarioRepository usuarioRepository;

    public Optional<PerfilNutricional> obtenerPorIdUsuario(Long idUsuario) {
        return perfilRepository.findById(idUsuario);
    }

    public PerfilNutricional crear(Long idUsuario, PerfilNutricional perfil) {
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        perfil.setIdUsuario(idUsuario);
        perfil.setUsuario(usuario);
        perfil.setFechaActualizacion(LocalDateTime.now());
        return perfilRepository.save(perfil);
    }

    public PerfilNutricional actualizar(Long idUsuario, PerfilNutricional perfilActualizado) {
        return perfilRepository.findById(idUsuario)
                .map(perfil -> {
                    perfil.setSexo(perfilActualizado.getSexo());
                    perfil.setFechaNacimiento(perfilActualizado.getFechaNacimiento());
                    perfil.setAlturaCm(perfilActualizado.getAlturaCm());
                    perfil.setPesoKg(perfilActualizado.getPesoKg());
                    perfil.setNivelActividad(perfilActualizado.getNivelActividad());
                    perfil.setObjetivo(perfilActualizado.getObjetivo());
                    perfil.setFechaActualizacion(LocalDateTime.now());
                    return perfilRepository.save(perfil);
                })
                .orElseThrow(() -> new RuntimeException("Perfil no encontrado"));
    }

    public void eliminar(Long idUsuario) {
        perfilRepository.deleteById(idUsuario);
    }
}