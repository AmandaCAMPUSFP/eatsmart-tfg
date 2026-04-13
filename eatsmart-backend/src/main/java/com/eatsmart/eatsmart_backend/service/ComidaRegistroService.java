package com.eatsmart.eatsmart_backend.service;

import com.eatsmart.eatsmart_backend.entity.ComidaRegistro;
import com.eatsmart.eatsmart_backend.entity.Usuario;
import com.eatsmart.eatsmart_backend.repository.ComidaRegistroRepository;
import com.eatsmart.eatsmart_backend.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class ComidaRegistroService {

    private final ComidaRegistroRepository comidaRepository;
    private final UsuarioRepository usuarioRepository;

    public List<ComidaRegistro> obtenerTodas() {
        return comidaRepository.findAll();
    }

    public Optional<ComidaRegistro> obtenerPorId(Long id) {
        return comidaRepository.findById(id);
    }

    public List<ComidaRegistro> obtenerPorUsuario(Long idUsuario) {
        return comidaRepository.findByUsuarioIdUsuario(idUsuario);
    }

    public List<ComidaRegistro> obtenerPorUsuarioYFecha(Long idUsuario, LocalDate fecha) {
        return comidaRepository.findByUsuarioIdUsuarioAndFecha(idUsuario, fecha);
    }

    public ComidaRegistro crear(ComidaRegistro comida) {
        comida.setFechaCreacion(LocalDateTime.now());
        return comidaRepository.save(comida);
    }

    public ComidaRegistro actualizar(Long id, ComidaRegistro comidaActualizada) {
        return comidaRepository.findById(id)
                .map(comida -> {
                    comida.setFecha(comidaActualizada.getFecha());
                    comida.setTipoComida(comidaActualizada.getTipoComida());
                    comida.setAlimentos(comidaActualizada.getAlimentos());
                    comida.setRecetas(comidaActualizada.getRecetas());
                    return comidaRepository.save(comida);
                })
                .orElseThrow(() -> new RuntimeException("Comida no encontrada"));
    }

    public void eliminar(Long id) {
        comidaRepository.deleteById(id);
    }
}