package com.eatsmart.eatsmart_backend.controller;

import com.eatsmart.eatsmart_backend.entity.PerfilNutricional;
import com.eatsmart.eatsmart_backend.service.PerfilNutricionalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/perfiles-nutricionales")
@RequiredArgsConstructor
public class PerfilNutricionalController {

    private final PerfilNutricionalService perfilService;

    @GetMapping("/{idUsuario}")
    public ResponseEntity<PerfilNutricional> obtenerPorIdUsuario(@PathVariable Long idUsuario) {
        return perfilService.obtenerPorIdUsuario(idUsuario)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{idUsuario}")
    public ResponseEntity<PerfilNutricional> crear(@PathVariable Long idUsuario, @RequestBody PerfilNutricional perfil) {
        return ResponseEntity.ok(perfilService.crear(idUsuario, perfil));
    }

    @PutMapping("/{idUsuario}")
    public ResponseEntity<PerfilNutricional> actualizar(@PathVariable Long idUsuario, @RequestBody PerfilNutricional perfil) {
        return ResponseEntity.ok(perfilService.actualizar(idUsuario, perfil));
    }

    @DeleteMapping("/{idUsuario}")
    public ResponseEntity<Void> eliminar(@PathVariable Long idUsuario) {
        perfilService.eliminar(idUsuario);
        return ResponseEntity.noContent().build();
    }
}