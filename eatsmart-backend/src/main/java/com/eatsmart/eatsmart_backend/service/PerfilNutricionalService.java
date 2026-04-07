package com.eatsmart.eatsmart_backend.service;

import com.eatsmart.eatsmart_backend.entity.PerfilNutricional;
import com.eatsmart.eatsmart_backend.repository.PerfilNutricionalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class PerfilNutricionalService {

    @Autowired
    private PerfilNutricionalRepository perfilNutricionalRepository;

    public List<PerfilNutricional> obtenerTodos() {
        return perfilNutricionalRepository.findAll();
    }

    public Optional<PerfilNutricional> obtenerPorId(Long id) {
        return perfilNutricionalRepository.findById(id);
    }

    public PerfilNutricional guardar(PerfilNutricional perfilNutricional) {
        return perfilNutricionalRepository.save(perfilNutricional);
    }

    public void eliminar(Long id) {
        perfilNutricionalRepository.deleteById(id);
    }
}