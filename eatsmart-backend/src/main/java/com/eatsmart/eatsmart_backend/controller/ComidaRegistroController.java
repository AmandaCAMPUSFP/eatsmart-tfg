package com.eatsmart.eatsmart_backend.controller;

import com.eatsmart.eatsmart_backend.entity.ComidaRegistro;
import com.eatsmart.eatsmart_backend.service.ComidaRegistroService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/comidas")
@RequiredArgsConstructor
public class ComidaRegistroController {

    private final ComidaRegistroService comidaService;

    @GetMapping
    public ResponseEntity<List<ComidaRegistro>> obtenerTodas() {
        return ResponseEntity.ok(comidaService.obtenerTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ComidaRegistro> obtenerPorId(@PathVariable Long id) {
        return comidaService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<List<ComidaRegistro>> obtenerPorUsuario(@PathVariable Long idUsuario) {
        return ResponseEntity.ok(comidaService.obtenerPorUsuario(idUsuario));
    }

    @GetMapping("/usuario/{idUsuario}/fecha/{fecha}")
    public ResponseEntity<List<ComidaRegistro>> obtenerPorUsuarioYFecha(
            @PathVariable Long idUsuario,
            @PathVariable LocalDate fecha) {
        return ResponseEntity.ok(comidaService.obtenerPorUsuarioYFecha(idUsuario, fecha));
    }

    @PostMapping
    public ResponseEntity<ComidaRegistro> crear(@RequestBody ComidaRegistro comida) {
        return ResponseEntity.ok(comidaService.crear(comida));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ComidaRegistro> actualizar(@PathVariable Long id, @RequestBody ComidaRegistro comida) {
        return ResponseEntity.ok(comidaService.actualizar(id, comida));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        comidaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}