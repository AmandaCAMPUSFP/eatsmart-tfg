package com.eatsmart.eatsmart_backend.controller;

import com.eatsmart.eatsmart_backend.entity.Alimento;
import com.eatsmart.eatsmart_backend.service.AlimentoService;
import lombok.RequiredArgsConstructor;
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
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Alimento> crear(@RequestBody Alimento alimento) {
        return ResponseEntity.ok(alimentoService.crear(alimento));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Alimento> actualizar(@PathVariable Long id, @RequestBody Alimento alimento) {
        return ResponseEntity.ok(alimentoService.actualizar(id, alimento));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        alimentoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}