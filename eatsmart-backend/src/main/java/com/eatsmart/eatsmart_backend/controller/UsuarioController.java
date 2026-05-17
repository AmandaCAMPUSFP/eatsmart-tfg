package com.eatsmart.eatsmart_backend.controller;

import com.eatsmart.eatsmart_backend.dto.UsuarioDTO;
import com.eatsmart.eatsmart_backend.entity.Usuario;
import com.eatsmart.eatsmart_backend.exception.ResourceNotFoundException;
import com.eatsmart.eatsmart_backend.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    // El listado completo de usuarios queda DESHABILITADO por seguridad.
    // Exponer todos los usuarios (incluyendo hashes) es una vulnerabilidad
    // (OWASP A01). Si se necesitase en el futuro, sería un endpoint solo para rol ADMIN.
    @GetMapping
    public ResponseEntity<String> obtenerTodos() {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body("Operación no permitida");
    }

    @GetMapping("/{id}")
    @PreAuthorize("@securityService.isOwner(#id)")
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
    @PreAuthorize("@securityService.isOwner(#id)")
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
    @PreAuthorize("@securityService.isOwner(#id)")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        usuarioService.obtenerPorId(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario con ID " + id + " no encontrado"));

        usuarioService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}