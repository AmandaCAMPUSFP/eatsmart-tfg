package com.eatsmart.eatsmart_backend.controller;

import com.eatsmart.eatsmart_backend.dto.ComidaRegistroDTO;
import com.eatsmart.eatsmart_backend.entity.Alimento;
import com.eatsmart.eatsmart_backend.entity.ComidaRegistro;
import com.eatsmart.eatsmart_backend.entity.Receta;
import com.eatsmart.eatsmart_backend.entity.Usuario;
import com.eatsmart.eatsmart_backend.exception.ResourceNotFoundException;
import com.eatsmart.eatsmart_backend.service.AlimentoService;
import com.eatsmart.eatsmart_backend.service.ComidaRegistroService;
import com.eatsmart.eatsmart_backend.service.RecetaService;
import com.eatsmart.eatsmart_backend.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/comidas")
@RequiredArgsConstructor
public class ComidaRegistroController {

    private final ComidaRegistroService comidaService;
    private final UsuarioService usuarioService;
    private final AlimentoService alimentoService;
    private final RecetaService recetaService;

    @GetMapping
    public ResponseEntity<List<ComidaRegistro>> obtenerTodas() {
        return ResponseEntity.ok(comidaService.obtenerTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ComidaRegistro> obtenerPorId(@PathVariable Long id) {
        return comidaService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException("Comida con ID " + id + " no encontrada"));
    }

    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<List<ComidaRegistro>> obtenerPorUsuario(@PathVariable Long idUsuario) {
        usuarioService.obtenerPorId(idUsuario)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario con ID " + idUsuario + " no encontrado"));

        return ResponseEntity.ok(comidaService.obtenerPorUsuario(idUsuario));
    }

    @GetMapping("/usuario/{idUsuario}/fecha/{fecha}")
    public ResponseEntity<List<ComidaRegistro>> obtenerPorUsuarioYFecha(
            @PathVariable Long idUsuario,
            @PathVariable LocalDate fecha) {

        usuarioService.obtenerPorId(idUsuario)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario con ID " + idUsuario + " no encontrado"));

        return ResponseEntity.ok(comidaService.obtenerPorUsuarioYFecha(idUsuario, fecha));
    }

    @PostMapping
    public ResponseEntity<ComidaRegistro> crear(@Valid @RequestBody ComidaRegistroDTO comidaDTO) {
        Usuario usuario = usuarioService.obtenerPorId(comidaDTO.getIdUsuario())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario con ID " + comidaDTO.getIdUsuario() + " no encontrado"));

        ComidaRegistro comida = new ComidaRegistro();
        comida.setUsuario(usuario);
        comida.setFecha(comidaDTO.getFecha());
        comida.setTipoComida(comidaDTO.getTipoComida());

        // Cargar alimentos
        List<Alimento> alimentos = new ArrayList<>();
        if (comidaDTO.getIdAlimentos() != null) {
            for (Long idAlimento : comidaDTO.getIdAlimentos()) {
                Alimento alimento = alimentoService.obtenerPorId(idAlimento)
                        .orElseThrow(() -> new ResourceNotFoundException("Alimento con ID " + idAlimento + " no encontrado"));
                alimentos.add(alimento);
            }
        }
        comida.setAlimentos(alimentos);

        // Cargar recetas
        List<Receta> recetas = new ArrayList<>();
        if (comidaDTO.getIdRecetas() != null) {
            for (Long idReceta : comidaDTO.getIdRecetas()) {
                Receta receta = recetaService.obtenerPorId(idReceta)
                        .orElseThrow(() -> new ResourceNotFoundException("Receta con ID " + idReceta + " no encontrada"));
                recetas.add(receta);
            }
        }
        comida.setRecetas(recetas);

        ComidaRegistro creada = comidaService.crear(comida);
        return ResponseEntity.status(HttpStatus.CREATED).body(creada);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ComidaRegistro> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody ComidaRegistroDTO comidaDTO) {

        ComidaRegistro comida = comidaService.obtenerPorId(id)
                .orElseThrow(() -> new ResourceNotFoundException("Comida con ID " + id + " no encontrada"));

        comida.setFecha(comidaDTO.getFecha());
        comida.setTipoComida(comidaDTO.getTipoComida());

        // Actualizar alimentos
        List<Alimento> alimentos = new ArrayList<>();
        if (comidaDTO.getIdAlimentos() != null) {
            for (Long idAlimento : comidaDTO.getIdAlimentos()) {
                Alimento alimento = alimentoService.obtenerPorId(idAlimento)
                        .orElseThrow(() -> new ResourceNotFoundException("Alimento con ID " + idAlimento + " no encontrado"));
                alimentos.add(alimento);
            }
        }
        comida.setAlimentos(alimentos);

        // Actualizar recetas
        List<Receta> recetas = new ArrayList<>();
        if (comidaDTO.getIdRecetas() != null) {
            for (Long idReceta : comidaDTO.getIdRecetas()) {
                Receta receta = recetaService.obtenerPorId(idReceta)
                        .orElseThrow(() -> new ResourceNotFoundException("Receta con ID " + idReceta + " no encontrada"));
                recetas.add(receta);
            }
        }
        comida.setRecetas(recetas);

        ComidaRegistro actualizada = comidaService.actualizar(id, comida);
        return ResponseEntity.ok(actualizada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        comidaService.obtenerPorId(id)
                .orElseThrow(() -> new ResourceNotFoundException("Comida con ID " + id + " no encontrada"));

        comidaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}