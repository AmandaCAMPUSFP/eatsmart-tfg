package com.eatsmart.eatsmart_backend.controller;

import com.eatsmart.eatsmart_backend.entity.Alimento;
import com.eatsmart.eatsmart_backend.service.AlimentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/alimentos")
public class AlimentoController {

    @Autowired
    private AlimentoService alimentoService;

    // GET - Obtener todos los alimentos
    @GetMapping
    public ResponseEntity<List<Alimento>> obtenerTodos() {
        List<Alimento> alimentos = alimentoService.obtenerTodos();
        return ResponseEntity.ok(alimentos);
    }

    // GET - Obtener alimento por ID
    @GetMapping("/{id}")
    public ResponseEntity<Alimento> obtenerPorId(@PathVariable Long id) {
        Optional<Alimento> alimento = alimentoService.obtenerPorId(id);
        return alimento.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // POST - Crear nuevo alimento
    @PostMapping
    public ResponseEntity<Alimento> crear(@RequestBody Alimento alimento) {
        Alimento alimentoGuardado = alimentoService.guardar(alimento);
        return ResponseEntity.status(HttpStatus.CREATED).body(alimentoGuardado);
    }

    // DELETE - Eliminar alimento
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        alimentoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}