package com.eatsmart.eatsmart_backend.controller;

import com.eatsmart.eatsmart_backend.dto.RecetaDTO;
import com.eatsmart.eatsmart_backend.entity.Alimento;
import com.eatsmart.eatsmart_backend.entity.Receta;
import com.eatsmart.eatsmart_backend.exception.ResourceNotFoundException;
import com.eatsmart.eatsmart_backend.service.AlimentoService;
import com.eatsmart.eatsmart_backend.service.RecetaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/recetas")
@RequiredArgsConstructor
public class RecetaController {

    private final RecetaService recetaService;
    private final AlimentoService alimentoService;

    @GetMapping
    public ResponseEntity<List<Receta>> obtenerTodas() {
        return ResponseEntity.ok(recetaService.obtenerTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Receta> obtenerPorId(@PathVariable Long id) {
        return recetaService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException("Receta con ID " + id + " no encontrada"));
    }

    @PostMapping
    public ResponseEntity<Receta> crear(@Valid @RequestBody RecetaDTO recetaDTO) {
        Receta receta = new Receta();
        receta.setNombre(recetaDTO.getNombre());
        receta.setDescripcion(recetaDTO.getDescripcion());
        receta.setRaciones(recetaDTO.getRaciones());
        receta.setTotalKcal(recetaDTO.getTotalKcal());
        receta.setTotalProteinas(recetaDTO.getTotalProteinas());
        receta.setTotalCarbohidratos(recetaDTO.getTotalCarbohidratos());
        receta.setTotalGrasas(recetaDTO.getTotalGrasas());

        // Cargar alimentos por IDs
        List<Alimento> alimentos = new ArrayList<>();
        if (recetaDTO.getIdAlimentos() != null) {
            for (Long idAlimento : recetaDTO.getIdAlimentos()) {
                Alimento alimento = alimentoService.obtenerPorId(idAlimento)
                        .orElseThrow(() -> new ResourceNotFoundException("Alimento con ID " + idAlimento + " no encontrado"));
                alimentos.add(alimento);
            }
        }
        receta.setAlimentos(alimentos);

        Receta creada = recetaService.crear(receta);
        return ResponseEntity.status(HttpStatus.CREATED).body(creada);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Receta> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody RecetaDTO recetaDTO) {

        Receta receta = recetaService.obtenerPorId(id)
                .orElseThrow(() -> new ResourceNotFoundException("Receta con ID " + id + " no encontrada"));

        receta.setNombre(recetaDTO.getNombre());
        receta.setDescripcion(recetaDTO.getDescripcion());
        receta.setRaciones(recetaDTO.getRaciones());
        receta.setTotalKcal(recetaDTO.getTotalKcal());
        receta.setTotalProteinas(recetaDTO.getTotalProteinas());
        receta.setTotalCarbohidratos(recetaDTO.getTotalCarbohidratos());
        receta.setTotalGrasas(recetaDTO.getTotalGrasas());

        // Actualizar alimentos
        List<Alimento> alimentos = new ArrayList<>();
        if (recetaDTO.getIdAlimentos() != null) {
            for (Long idAlimento : recetaDTO.getIdAlimentos()) {
                Alimento alimento = alimentoService.obtenerPorId(idAlimento)
                        .orElseThrow(() -> new ResourceNotFoundException("Alimento con ID " + idAlimento + " no encontrado"));
                alimentos.add(alimento);
            }
        }
        receta.setAlimentos(alimentos);

        Receta actualizada = recetaService.actualizar(id, receta);
        return ResponseEntity.ok(actualizada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        recetaService.obtenerPorId(id)
                .orElseThrow(() -> new ResourceNotFoundException("Receta con ID " + id + " no encontrada"));

        recetaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}