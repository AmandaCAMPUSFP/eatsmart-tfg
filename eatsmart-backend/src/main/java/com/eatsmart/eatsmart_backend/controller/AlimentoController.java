package com.eatsmart.eatsmart_backend.controller;

import com.eatsmart.eatsmart_backend.dto.AlimentoDTO;
import com.eatsmart.eatsmart_backend.entity.Alimento;
import com.eatsmart.eatsmart_backend.exception.ResourceNotFoundException;
import com.eatsmart.eatsmart_backend.service.AlimentoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/alimentos")
@RequiredArgsConstructor
public class AlimentoController {

    private final AlimentoService alimentoService;

    @GetMapping
    public ResponseEntity<List<Alimento>> obtenerTodos() {
        return ResponseEntity.ok(alimentoService.obtenerTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Alimento> obtenerPorId(@PathVariable Long id) {
        return alimentoService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException("Alimento con ID " + id + " no encontrado"));
    }

    @PostMapping
    public ResponseEntity<Alimento> crear(@Valid @RequestBody AlimentoDTO alimentoDTO) {
        Alimento alimento = new Alimento();
        alimento.setNombre(alimentoDTO.getNombre());
        alimento.setKcal100g(alimentoDTO.getKcal100g());
        alimento.setProteinas100g(alimentoDTO.getProteinas100g());
        alimento.setCarbohidratos100g(alimentoDTO.getCarbohidratos100g());
        alimento.setGrasas100g(alimentoDTO.getGrasas100g());

        Alimento creado = alimentoService.crear(alimento);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Alimento> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody AlimentoDTO alimentoDTO) {

        Alimento alimento = alimentoService.obtenerPorId(id)
                .orElseThrow(() -> new ResourceNotFoundException("Alimento con ID " + id + " no encontrado"));

        alimento.setNombre(alimentoDTO.getNombre());
        alimento.setKcal100g(alimentoDTO.getKcal100g());
        alimento.setProteinas100g(alimentoDTO.getProteinas100g());
        alimento.setCarbohidratos100g(alimentoDTO.getCarbohidratos100g());
        alimento.setGrasas100g(alimentoDTO.getGrasas100g());

        Alimento actualizado = alimentoService.actualizar(id, alimento);
        return ResponseEntity.ok(actualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        alimentoService.obtenerPorId(id)
                .orElseThrow(() -> new ResourceNotFoundException("Alimento con ID " + id + " no encontrado"));

        alimentoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}