package com.eatsmart.eatsmart_backend.controller;

import com.eatsmart.eatsmart_backend.dto.UsuarioDTO;
import com.eatsmart.eatsmart_backend.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para la gestión de usuarios.
 * Expone los endpoints bajo el prefijo {@code /usuarios}.
 * Dado que el contexto de la aplicación ya está configurado con
 * {@code /api}, la ruta completa resulta {@code /api/usuarios}.
 */
@RestController
@RequestMapping("/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    /**
     * Devuelve el listado completo de usuarios registrados.
     *
     * @return 200 OK con la lista de {@link UsuarioDTO}
     */
    @GetMapping
    public ResponseEntity<List<UsuarioDTO>> getAll() {
        return ResponseEntity.ok(usuarioService.getAll());
    }

    /**
     * Busca un usuario por su identificador.
     *
     * @param id identificador del usuario
     * @return 200 OK con el {@link UsuarioDTO} o 404 Not Found si no existe
     */
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDTO> getById(@PathVariable Long id) {
        return usuarioService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Registra un nuevo usuario en el sistema.
     *
     * @param dto datos del usuario a registrar
     * @return 201 Created con el {@link UsuarioDTO} persistido
     */
    @PostMapping
    public ResponseEntity<UsuarioDTO> save(@RequestBody UsuarioDTO dto) {
        UsuarioDTO registrado = usuarioService.save(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(registrado);
    }
}
