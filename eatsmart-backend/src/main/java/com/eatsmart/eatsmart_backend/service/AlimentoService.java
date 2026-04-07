package com.eatsmart.eatsmart_backend.service;

import com.eatsmart.eatsmart_backend.entity.Alimento;
import com.eatsmart.eatsmart_backend.repository.AlimentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class AlimentoService {

    @Autowired
    private AlimentoRepository alimentoRepository;

    // Obtener todos los alimentos
    public List<Alimento> obtenerTodos() {
        return alimentoRepository.findAll();
    }

    // Obtener alimento por ID
    public Optional<Alimento> obtenerPorId(Long id) {
        return alimentoRepository.findById(id);
    }

    // Guardar un nuevo alimento
    public Alimento guardar(Alimento alimento) {
        return alimentoRepository.save(alimento);
    }

    // Eliminar alimento
    public void eliminar(Long id) {
        alimentoRepository.deleteById(id);
    }
}