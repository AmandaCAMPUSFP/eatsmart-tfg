package com.eatsmart.eatsmart_backend.controller;

import com.eatsmart.eatsmart_backend.entity.PerfilNutricional;
import com.eatsmart.eatsmart_backend.service.PerfilNutricionalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/perfiles-nutricionales")
public class PerfilNutricionalController {

    @Autowired
    private PerfilNutricionalService perfilNutricionalService;

    @GetMapping
    public ResponseEntity<List<PerfilNutricional>> obtenerTodos() {
        List<PerfilNutricional> perfiles = perfilNutricionalService.obtenerTodos();
        return ResponseEntity.ok(perfiles);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PerfilNutricional> obtenerPorId(@PathVariable Long id) {
        Optional<PerfilNutricional> perfil = perfilNutricionalService.obtenerPorId(id);
        return perfil.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<PerfilNutricional> crear(@RequestBody PerfilNutricional perfilNutricional) {
        PerfilNutricional perfilGuardado = perfilNutricionalService.guardar(perfilNutricional);
        return ResponseEntity.status(HttpStatus.CREATED).body(perfilGuardado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        perfilNutricionalService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}