package com.eatsmart.eatsmart_backend.controller;

import com.eatsmart.eatsmart_backend.dto.UsuarioDTO;
import com.eatsmart.eatsmart_backend.entity.Usuario;
import com.eatsmart.eatsmart_backend.exception.ResourceNotFoundException;
import com.eatsmart.eatsmart_backend.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    @GetMapping
    public ResponseEntity<List<Usuario>> obtenerTodos() {
        return ResponseEntity.ok(usuarioService.obtenerTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> obtenerPorId(@PathVariable Long id) {
        return usuarioService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario con ID " + id + " no encontrado"));
    }

    @PostMapping
    public ResponseEntity<Usuario> crear(@Valid @RequestBody UsuarioDTO usuarioDTO) {
        Usuario usuario = new Usuario();
        usuario.setEmail(usuarioDTO.getEmail());
        usuario.setContrasenaHash(usuarioDTO.getContrasena());
        usuario.setActivo(usuarioDTO.getActivo() != null ? usuarioDTO.getActivo() : "S");

        Usuario creado = usuarioService.crear(usuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Usuario> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody UsuarioDTO usuarioDTO) {

        Usuario usuario = usuarioService.obtenerPorId(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario con ID " + id + " no encontrado"));

        usuario.setEmail(usuarioDTO.getEmail());
        usuario.setActivo(usuarioDTO.getActivo() != null ? usuarioDTO.getActivo() : usuario.getActivo());

        Usuario actualizado = usuarioService.actualizar(id, usuario);
        return ResponseEntity.ok(actualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        usuarioService.obtenerPorId(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario con ID " + id + " no encontrado"));

        usuarioService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}