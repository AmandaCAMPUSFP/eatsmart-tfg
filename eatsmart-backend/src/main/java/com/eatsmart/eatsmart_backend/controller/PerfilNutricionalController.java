package com.eatsmart.eatsmart_backend.controller;

import com.eatsmart.eatsmart_backend.dto.PerfilNutricionalDTO;
import com.eatsmart.eatsmart_backend.entity.PerfilNutricional;
import com.eatsmart.eatsmart_backend.exception.ResourceNotFoundException;
import com.eatsmart.eatsmart_backend.service.PerfilNutricionalService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
                .orElseThrow(() -> new ResourceNotFoundException("Perfil nutricional del usuario " + idUsuario + " no encontrado"));
    }

    @PostMapping("/{idUsuario}")
    public ResponseEntity<PerfilNutricional> crear(
            @PathVariable Long idUsuario,
            @Valid @RequestBody PerfilNutricionalDTO perfilDTO) {

        PerfilNutricional perfil = new PerfilNutricional();
        perfil.setIdUsuario(idUsuario);
        perfil.setSexo(perfilDTO.getSexo());
        perfil.setFechaNacimiento(perfilDTO.getFechaNacimiento());
        perfil.setAlturaCm(perfilDTO.getAlturaCm());
        perfil.setPesoKg(perfilDTO.getPesoKg());
        perfil.setNivelActividad(perfilDTO.getNivelActividad());
        perfil.setObjetivo(perfilDTO.getObjetivo());

        PerfilNutricional creado = perfilService.crear(idUsuario, perfil);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    @PutMapping("/{idUsuario}")
    public ResponseEntity<PerfilNutricional> actualizar(
            @PathVariable Long idUsuario,
            @Valid @RequestBody PerfilNutricionalDTO perfilDTO) {

        PerfilNutricional perfil = perfilService.obtenerPorIdUsuario(idUsuario)
                .orElseThrow(() -> new ResourceNotFoundException("Perfil nutricional del usuario " + idUsuario + " no encontrado"));

        perfil.setSexo(perfilDTO.getSexo());
        perfil.setFechaNacimiento(perfilDTO.getFechaNacimiento());
        perfil.setAlturaCm(perfilDTO.getAlturaCm());
        perfil.setPesoKg(perfilDTO.getPesoKg());
        perfil.setNivelActividad(perfilDTO.getNivelActividad());
        perfil.setObjetivo(perfilDTO.getObjetivo());

        PerfilNutricional actualizado = perfilService.actualizar(idUsuario, perfil);
        return ResponseEntity.ok(actualizado);
    }

    @DeleteMapping("/{idUsuario}")
    public ResponseEntity<Void> eliminar(@PathVariable Long idUsuario) {
        perfilService.obtenerPorIdUsuario(idUsuario)
                .orElseThrow(() -> new ResourceNotFoundException("Perfil nutricional del usuario " + idUsuario + " no encontrado"));

        perfilService.eliminar(idUsuario);
        return ResponseEntity.noContent().build();
    }
}