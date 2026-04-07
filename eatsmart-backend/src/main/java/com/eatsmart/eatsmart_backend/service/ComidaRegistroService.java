package com.eatsmart.eatsmart_backend.service;

import com.eatsmart.eatsmart_backend.entity.ComidaRegistro;
import com.eatsmart.eatsmart_backend.repository.ComidaRegistroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ComidaRegistroService {

    @Autowired
    private ComidaRegistroRepository comidaRegistroRepository;

    public List<ComidaRegistro> obtenerTodos() {
        return comidaRegistroRepository.findAll();
    }

    public Optional<ComidaRegistro> obtenerPorId(Long id) {
        return comidaRegistroRepository.findById(id);
    }

    public ComidaRegistro guardar(ComidaRegistro comidaRegistro) {
        return comidaRegistroRepository.save(comidaRegistro);
    }

    public void eliminar(Long id) {
        comidaRegistroRepository.deleteById(id);
    }
}