package com.eatsmart.eatsmart_backend.service;

import com.eatsmart.eatsmart_backend.dto.AlimentoDTO;
import com.eatsmart.eatsmart_backend.entity.Alimento;
import com.eatsmart.eatsmart_backend.repository.AlimentoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Servicio que gestiona la lógica de negocio relacionada con los alimentos.
 * Actúa como intermediario entre el controlador REST y el repositorio JPA,
 * aplicando las transformaciones y validaciones necesarias.
 */
@Service
@RequiredArgsConstructor
public class AlimentoService {

    private final AlimentoRepository alimentoRepository;

    /**
     * Recupera el listado completo de alimentos disponibles en el catálogo.
     *
     * @return lista de {@link AlimentoDTO} con todos los alimentos
     */
    public List<AlimentoDTO> getAll() {
        return alimentoRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Busca un alimento por su identificador único.
     *
     * @param id identificador del alimento
     * @return {@link Optional} con el {@link AlimentoDTO} si existe, vacío en caso contrario
     */
    public Optional<AlimentoDTO> getById(Long id) {
        return alimentoRepository.findById(id).map(this::toDTO);
    }

    /**
     * Persiste un nuevo alimento en la base de datos.
     * Establece automáticamente la fecha de creación si no se ha proporcionado.
     *
     * @param dto datos del alimento a guardar
     * @return {@link AlimentoDTO} con los datos guardados, incluyendo el identificador generado
     * @throws IllegalArgumentException si el nombre del alimento es nulo o vacío
     */
    public AlimentoDTO save(AlimentoDTO dto) {
        if (dto.getNombre() == null || dto.getNombre().isBlank()) {
            throw new IllegalArgumentException("El nombre del alimento es obligatorio.");
        }
        Alimento alimento = toEntity(dto);
        alimento.setFechaCreacion(LocalDateTime.now());
        return toDTO(alimentoRepository.save(alimento));
    }

    // ── Métodos auxiliares de conversión ─────────────────────────────────────

    private AlimentoDTO toDTO(Alimento alimento) {
        return new AlimentoDTO(
                alimento.getIdAlimento(),
                alimento.getNombre(),
                alimento.getKcal100g(),
                alimento.getProteinas100g(),
                alimento.getCarbohidratos100g(),
                alimento.getGrasas100g()
        );
    }

    private Alimento toEntity(AlimentoDTO dto) {
        Alimento alimento = new Alimento();
        alimento.setIdAlimento(dto.getIdAlimento());
        alimento.setNombre(dto.getNombre());
        alimento.setKcal100g(dto.getKcal100g());
        alimento.setProteinas100g(dto.getProteinas100g());
        alimento.setCarbohidratos100g(dto.getCarbohidratos100g());
        alimento.setGrasas100g(dto.getGrasas100g());
        return alimento;
    }
}
