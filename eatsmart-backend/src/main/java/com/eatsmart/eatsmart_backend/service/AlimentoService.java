package com.eatsmart.eatsmart_backend.service;

import com.eatsmart.eatsmart_backend.entity.Alimento;
import com.eatsmart.eatsmart_backend.repository.AlimentoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class AlimentoService {

    private final AlimentoRepository alimentoRepository;

    public List<Alimento> obtenerTodos() {
        return alimentoRepository.findAll();
    }

    public Optional<Alimento> obtenerPorId(Long id) {
        return alimentoRepository.findById(id);
    }

    public Alimento crear(Alimento alimento) {
        alimento.setFechaCreacion(LocalDateTime.now());
        return alimentoRepository.save(alimento);
    }

    public Alimento actualizar(Long id, Alimento alimentoActualizado) {
        return alimentoRepository.findById(id)
                .map(alimento -> {
                    alimento.setNombre(alimentoActualizado.getNombre());
                    alimento.setKcal100g(alimentoActualizado.getKcal100g());
                    alimento.setProteinas100g(alimentoActualizado.getProteinas100g());
                    alimento.setCarbohidratos100g(alimentoActualizado.getCarbohidratos100g());
                    alimento.setGrasas100g(alimentoActualizado.getGrasas100g());
                    return alimentoRepository.save(alimento);
                })
                .orElseThrow(() -> new RuntimeException("Alimento no encontrado"));
    }

    public void eliminar(Long id) {
        alimentoRepository.deleteById(id);
    }
}