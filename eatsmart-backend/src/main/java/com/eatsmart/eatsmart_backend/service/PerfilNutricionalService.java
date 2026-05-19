package com.eatsmart.eatsmart_backend.service;

import com.eatsmart.eatsmart_backend.entity.PerfilNutricional;
import com.eatsmart.eatsmart_backend.repository.PerfilNutricionalRepository;
import com.eatsmart.eatsmart_backend.repository.UsuarioRepository;
import jakarta.persistence.EntityManager;
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
    private final EntityManager entityManager;

    public Optional<PerfilNutricional> obtenerPorIdUsuario(Long idUsuario) {
        return perfilRepository.findById(idUsuario);
    }

    public PerfilNutricional crear(Long idUsuario, PerfilNutricional perfil) {
        // Verificar que el usuario existe
        if (!usuarioRepository.existsById(idUsuario)) {
            throw new RuntimeException("Usuario no encontrado");
        }

        // El id del perfil ES el id del usuario (clave primaria compartida).
        // NO asignamos el objeto Usuario: la relación es de solo lectura
        // (insertable=false, updatable=false), asignarla confunde a Hibernate.
        perfil.setIdUsuario(idUsuario);
        perfil.setUsuario(null);
        perfil.setFechaActualizacion(LocalDateTime.now());

        // Usamos persist() (no save/merge) porque es una entidad NUEVA
        // con ID asignado manualmente. Esto evita el "null identifier".
        entityManager.persist(perfil);
        entityManager.flush();

        return perfil;
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