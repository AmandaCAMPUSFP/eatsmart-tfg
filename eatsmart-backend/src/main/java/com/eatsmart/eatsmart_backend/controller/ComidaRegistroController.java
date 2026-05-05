package com.eatsmart.eatsmart_backend.controller;

import com.eatsmart.eatsmart_backend.dto.ComidaRegistroDTO;
import com.eatsmart.eatsmart_backend.entity.Alimento;
import com.eatsmart.eatsmart_backend.entity.ComidaRegistro;
import com.eatsmart.eatsmart_backend.entity.Receta;
import com.eatsmart.eatsmart_backend.entity.Usuario;
import com.eatsmart.eatsmart_backend.exception.ResourceNotFoundException;
import com.eatsmart.eatsmart_backend.exception.SecurityException;
import com.eatsmart.eatsmart_backend.service.AlimentoService;
import com.eatsmart.eatsmart_backend.service.ComidaRegistroService;
import com.eatsmart.eatsmart_backend.service.RecetaService;
import com.eatsmart.eatsmart_backend.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/comidas")
@RequiredArgsConstructor
public class ComidaRegistroController {

    private final ComidaRegistroService comidaService;
    private final UsuarioService usuarioService;
    private final AlimentoService alimentoService;
    private final RecetaService recetaService;

    /**
     * Obtener usuario autenticado
     */
    private String getEmailAutenticado() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated()) {
            return auth.getPrincipal().toString();
        }
        throw new SecurityException("Usuario no autenticado");
    }

    /**
     * Validar que el usuario es propietario
     */
    private void validarPropiedad(Long idUsuario) {
        String emailAutenticado = getEmailAutenticado();
        Usuario usuarioAutenticado = usuarioService.obtenerPorEmail(emailAutenticado)
                .orElseThrow(() -> new SecurityException("Usuario no encontrado"));

        if (!usuarioAutenticado.getIdUsuario().equals(idUsuario)) {
            log.warn("Intento de acceso no autorizado: usuario {} intentó acceder a datos de usuario {}",
                    usuarioAutenticado.getIdUsuario(), idUsuario);
            throw new SecurityException("No tienes permiso para acceder a estos datos");
        }
    }

    @GetMapping
    public ResponseEntity<List<ComidaRegistro>> obtenerTodas() {
        String emailAutenticado = getEmailAutenticado();
        Usuario usuario = usuarioService.obtenerPorEmail(emailAutenticado)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        return ResponseEntity.ok(comidaService.obtenerPorUsuario(usuario.getIdUsuario()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ComidaRegistro> obtenerPorId(@PathVariable Long id) {
        ComidaRegistro comida = comidaService.obtenerPorId(id)
                .orElseThrow(() -> new ResourceNotFoundException("Comida con ID " + id + " no encontrada"));

        // Validar que el usuario autenticado es el propietario
        validarPropiedad(comida.getUsuario().getIdUsuario());

        return ResponseEntity.ok(comida);
    }

    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<List<ComidaRegistro>> obtenerPorUsuario(@PathVariable Long idUsuario) {
        // Validar propiedad
        validarPropiedad(idUsuario);

        usuarioService.obtenerPorId(idUsuario)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario con ID " + idUsuario + " no encontrado"));

        return ResponseEntity.ok(comidaService.obtenerPorUsuario(idUsuario));
    }

    @GetMapping("/usuario/{idUsuario}/fecha/{fecha}")
    public ResponseEntity<List<ComidaRegistro>> obtenerPorUsuarioYFecha(
            @PathVariable Long idUsuario,
            @PathVariable LocalDate fecha) {

        // Validar propiedad
        validarPropiedad(idUsuario);

        usuarioService.obtenerPorId(idUsuario)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario con ID " + idUsuario + " no encontrado"));

        return ResponseEntity.ok(comidaService.obtenerPorUsuarioYFecha(idUsuario, fecha));
    }

    @PostMapping
    public ResponseEntity<ComidaRegistro> crear(@Valid @RequestBody ComidaRegistroDTO comidaDTO) {
        // Validar que el usuario autenticado es el propietario
        validarPropiedad(comidaDTO.getIdUsuario());

        Usuario usuario = usuarioService.obtenerPorId(comidaDTO.getIdUsuario())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario con ID " + comidaDTO.getIdUsuario() + " no encontrado"));

        ComidaRegistro comida = new ComidaRegistro();
        comida.setUsuario(usuario);
        comida.setFecha(comidaDTO.getFecha());
        comida.setTipoComida(comidaDTO.getTipoComida());

        // Cargar alimentos
        List<Alimento> alimentos = new ArrayList<>();
        if (comidaDTO.getIdAlimentos() != null) {
            for (Long idAlimento : comidaDTO.getIdAlimentos()) {
                Alimento alimento = alimentoService.obtenerPorId(idAlimento)
                        .orElseThrow(() -> new ResourceNotFoundException("Alimento con ID " + idAlimento + " no encontrado"));
                alimentos.add(alimento);
            }
        }
        comida.setAlimentos(alimentos);

        // Cargar recetas
        List<Receta> recetas = new ArrayList<>();
        if (comidaDTO.getIdRecetas() != null) {
            for (Long idReceta : comidaDTO.getIdRecetas()) {
                Receta receta = recetaService.obtenerPorId(idReceta)
                        .orElseThrow(() -> new ResourceNotFoundException("Receta con ID " + idReceta + " no encontrada"));
                recetas.add(receta);
            }
        }
        comida.setRecetas(recetas);

        ComidaRegistro creada = comidaService.crear(comida);

        // Logging de auditoría
        log.info("Comida creada: usuario={}, comidaId={}, fecha={}",
                usuario.getIdUsuario(), creada.getIdComida(), creada.getFecha());

        return ResponseEntity.status(HttpStatus.CREATED).body(creada);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ComidaRegistro> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody ComidaRegistroDTO comidaDTO) {

        ComidaRegistro comida = comidaService.obtenerPorId(id)
                .orElseThrow(() -> new ResourceNotFoundException("Comida con ID " + id + " no encontrada"));

        // Validar que el usuario autenticado es el propietario
        validarPropiedad(comida.getUsuario().getIdUsuario());

        // Validar que no intenta cambiar de usuario
        if (!comida.getUsuario().getIdUsuario().equals(comidaDTO.getIdUsuario())) {
            throw new SecurityException("No puedes cambiar el propietario de una comida");
        }

        comida.setFecha(comidaDTO.getFecha());
        comida.setTipoComida(comidaDTO.getTipoComida());

        // Actualizar alimentos
        List<Alimento> alimentos = new ArrayList<>();
        if (comidaDTO.getIdAlimentos() != null) {
            for (Long idAlimento : comidaDTO.getIdAlimentos()) {
                Alimento alimento = alimentoService.obtenerPorId(idAlimento)
                        .orElseThrow(() -> new ResourceNotFoundException("Alimento con ID " + idAlimento + " no encontrado"));
                alimentos.add(alimento);
            }
        }
        comida.setAlimentos(alimentos);

        // Actualizar recetas
        List<Receta> recetas = new ArrayList<>();
        if (comidaDTO.getIdRecetas() != null) {
            for (Long idReceta : comidaDTO.getIdRecetas()) {
                Receta receta = recetaService.obtenerPorId(idReceta)
                        .orElseThrow(() -> new ResourceNotFoundException("Receta con ID " + idReceta + " no encontrada"));
                recetas.add(receta);
            }
        }
        comida.setRecetas(recetas);

        ComidaRegistro actualizada = comidaService.actualizar(id, comida);

        // Logging de auditoría
        log.info("Comida actualizada: usuario={}, comidaId={}",
                comida.getUsuario().getIdUsuario(), id);

        return ResponseEntity.ok(actualizada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        ComidaRegistro comida = comidaService.obtenerPorId(id)
                .orElseThrow(() -> new ResourceNotFoundException("Comida con ID " + id + " no encontrada"));

        // Validar que el usuario autenticado es el propietario
        validarPropiedad(comida.getUsuario().getIdUsuario());

        comidaService.eliminar(id);

        // Logging de auditoría
        log.info("Comida eliminada: usuario={}, comidaId={}",
                comida.getUsuario().getIdUsuario(), id);

        return ResponseEntity.noContent().build();
    }
}