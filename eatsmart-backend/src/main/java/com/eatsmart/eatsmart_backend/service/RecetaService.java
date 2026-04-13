package com.eatsmart.eatsmart_backend.service;

import com.eatsmart.eatsmart_backend.entity.Receta;
import com.eatsmart.eatsmart_backend.repository.RecetaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class RecetaService {

    private final RecetaRepository recetaRepository;

    public List<Receta> obtenerTodas() {
        return recetaRepository.findAll();
    }

    public Optional<Receta> obtenerPorId(Long id) {
        return recetaRepository.findById(id);
    }

    public Receta crear(Receta receta) {
        receta.setFechaCreacion(LocalDateTime.now());
        return recetaRepository.save(receta);
    }

    public Receta actualizar(Long id, Receta recetaActualizada) {
        return recetaRepository.findById(id)
                .map(receta -> {
                    receta.setNombre(recetaActualizada.getNombre());
                    receta.setDescripcion(recetaActualizada.getDescripcion());
                    receta.setRaciones(recetaActualizada.getRaciones());
                    receta.setTotalKcal(recetaActualizada.getTotalKcal());
                    receta.setTotalProteinas(recetaActualizada.getTotalProteinas());
                    receta.setTotalCarbohidratos(recetaActualizada.getTotalCarbohidratos());
                    receta.setTotalGrasas(recetaActualizada.getTotalGrasas());
                    receta.setAlimentos(recetaActualizada.getAlimentos());
                    return recetaRepository.save(receta);
                })
                .orElseThrow(() -> new RuntimeException("Receta no encontrada"));
    }

    public void eliminar(Long id) {
        recetaRepository.deleteById(id);
    }
}