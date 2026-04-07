package com.eatsmart.eatsmart_backend.controller;

import com.eatsmart.eatsmart_backend.entity.ComidaRegistro;
import com.eatsmart.eatsmart_backend.service.ComidaRegistroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/comidas-registro")
public class ComidaRegistroController {

    @Autowired
    private ComidaRegistroService comidaRegistroService;

    @GetMapping
    public ResponseEntity<List<ComidaRegistro>> obtenerTodos() {
        List<ComidaRegistro> comidas = comidaRegistroService.obtenerTodos();
        return ResponseEntity.ok(comidas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ComidaRegistro> obtenerPorId(@PathVariable Long id) {
        Optional<ComidaRegistro> comida = comidaRegistroService.obtenerPorId(id);
        return comida.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ComidaRegistro> crear(@RequestBody ComidaRegistro comidaRegistro) {
        ComidaRegistro comidaGuardada = comidaRegistroService.guardar(comidaRegistro);
        return ResponseEntity.status(HttpStatus.CREATED).body(comidaGuardada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        comidaRegistroService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}