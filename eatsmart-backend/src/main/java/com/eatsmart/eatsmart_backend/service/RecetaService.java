package com.eatsmart.eatsmart_backend.service;

import com.eatsmart.eatsmart_backend.entity.Receta;
import com.eatsmart.eatsmart_backend.repository.RecetaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class RecetaService {

    @Autowired
    private RecetaRepository recetaRepository;

    public List<Receta> obtenerTodos() {
        return recetaRepository.findAll();
    }

    public Optional<Receta> obtenerPorId(Long id) {
        return recetaRepository.findById(id);
    }

    public Receta guardar(Receta receta) {
        return recetaRepository.save(receta);
    }

    public void eliminar(Long id) {
        recetaRepository.deleteById(id);
    }
}