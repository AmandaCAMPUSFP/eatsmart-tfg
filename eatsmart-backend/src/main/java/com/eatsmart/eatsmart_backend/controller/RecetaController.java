package com.eatsmart.eatsmart_backend.controller;

import com.eatsmart.eatsmart_backend.entity.Receta;
import com.eatsmart.eatsmart_backend.service.RecetaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/recetas")
public class RecetaController {

    @Autowired
    private RecetaService recetaService;

    @GetMapping
    public ResponseEntity<List<Receta>> obtenerTodos() {
        List<Receta> recetas = recetaService.obtenerTodos();
        return ResponseEntity.ok(recetas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Receta> obtenerPorId(@PathVariable Long id) {
        Optional<Receta> receta = recetaService.obtenerPorId(id);
        return receta.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Receta> crear(@RequestBody Receta receta) {
        Receta recetaGuardada = recetaService.guardar(receta);
        return ResponseEntity.status(HttpStatus.CREATED).body(recetaGuardada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        recetaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}