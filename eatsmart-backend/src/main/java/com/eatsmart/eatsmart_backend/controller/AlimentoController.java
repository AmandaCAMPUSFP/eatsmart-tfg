package com.eatsmart.eatsmart_backend.controller;

import com.eatsmart.eatsmart_backend.dto.AlimentoDTO;
import com.eatsmart.eatsmart_backend.service.AlimentoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para la gestión del catálogo de alimentos.
 * Expone los endpoints públicos bajo el prefijo {@code /alimentos}.
 * Dado que el contexto de la aplicación ya está configurado con
 * {@code /api}, la ruta completa resulta {@code /api/alimentos}.
 */
@RestController
@RequestMapping("/alimentos")
@RequiredArgsConstructor
public class AlimentoController {

    private final AlimentoService alimentoService;

    /**
     * Devuelve el listado completo de alimentos del catálogo.
     *
     * @return 200 OK con la lista de {@link AlimentoDTO}
     */
    @GetMapping
    public ResponseEntity<List<AlimentoDTO>> getAll() {
        return ResponseEntity.ok(alimentoService.getAll());
    }

    /**
     * Busca un alimento por su identificador.
     *
     * @param id identificador del alimento
     * @return 200 OK con el {@link AlimentoDTO} o 404 Not Found si no existe
     */
    @GetMapping("/{id}")
    public ResponseEntity<AlimentoDTO> getById(@PathVariable Long id) {
        return alimentoService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Crea un nuevo alimento en el catálogo.
     *
     * @param dto datos del alimento a crear
     * @return 201 Created con el {@link AlimentoDTO} persistido
     */
    @PostMapping
    public ResponseEntity<AlimentoDTO> save(@RequestBody AlimentoDTO dto) {
        AlimentoDTO guardado = alimentoService.save(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(guardado);
    }
}
