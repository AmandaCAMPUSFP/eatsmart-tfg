package com.eatsmart.eatsmart_backend;

import com.eatsmart.eatsmart_backend.entity.ComidaRegistro;
import com.eatsmart.eatsmart_backend.entity.Usuario;
import com.eatsmart.eatsmart_backend.repository.ComidaRegistroRepository;
import com.eatsmart.eatsmart_backend.repository.UsuarioRepository;
import com.eatsmart.eatsmart_backend.service.ComidaRegistroService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests del servicio de registro de comidas")
class ComidaRegistroServiceTest {

    @Mock
    private ComidaRegistroRepository comidaRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private ComidaRegistroService comidaService;

    private ComidaRegistro comida;
    private Usuario usuario;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setIdUsuario(1L);
        usuario.setEmail("test@eatsmart.com");

        comida = new ComidaRegistro();
        comida.setIdComida(10L);
        comida.setFecha(LocalDate.now());
        comida.setTipoComida("Desayuno");
        comida.setUsuario(usuario);
    }

    @Test
    @DisplayName("Crear una comida debe asignarle fecha de creación y guardarla")
    void crear_comidaValida_seGuardaConFechaCreacion() {
        when(comidaRepository.save(any(ComidaRegistro.class))).thenReturn(comida);

        ComidaRegistro resultado = comidaService.crear(comida);

        assertNotNull(resultado);
        assertNotNull(comida.getFechaCreacion());
        verify(comidaRepository).save(any(ComidaRegistro.class));
    }

    @Test
    @DisplayName("Obtener por id debe devolver la comida si existe")
    void obtenerPorId_comidaExiste_devuelveComida() {
        when(comidaRepository.findById(10L)).thenReturn(Optional.of(comida));

        Optional<ComidaRegistro> resultado = comidaService.obtenerPorId(10L);

        assertTrue(resultado.isPresent());
        assertEquals(10L, resultado.get().getIdComida());
    }

    @Test
    @DisplayName("Obtener por id que no existe debe devolver Optional vacío")
    void obtenerPorId_comidaNoExiste_devuelveVacio() {
        when(comidaRepository.findById(999L)).thenReturn(Optional.empty());

        Optional<ComidaRegistro> resultado = comidaService.obtenerPorId(999L);

        assertTrue(resultado.isEmpty());
    }

    @Test
    @DisplayName("Obtener por usuario debe devolver las comidas de ese usuario")
    void obtenerPorUsuario_devuelveListaDeComidas() {
        List<ComidaRegistro> comidas = Arrays.asList(comida);
        when(comidaRepository.findByUsuarioIdUsuario(1L)).thenReturn(comidas);

        List<ComidaRegistro> resultado = comidaService.obtenerPorUsuario(1L);

        assertEquals(1, resultado.size());
        assertEquals("test@eatsmart.com", resultado.get(0).getUsuario().getEmail());
    }

    @Test
    @DisplayName("Obtener por usuario y fecha debe filtrar por ambos criterios")
    void obtenerPorUsuarioYFecha_filtraCorrectamente() {
        LocalDate hoy = LocalDate.now();
        when(comidaRepository.findByUsuarioIdUsuarioAndFecha(1L, hoy))
                .thenReturn(Arrays.asList(comida));

        List<ComidaRegistro> resultado = comidaService.obtenerPorUsuarioYFecha(1L, hoy);

        assertEquals(1, resultado.size());
        verify(comidaRepository).findByUsuarioIdUsuarioAndFecha(1L, hoy);
    }

    @Test
    @DisplayName("Actualizar una comida existente debe modificar sus campos")
    void actualizar_comidaExistente_seActualizaCorrectamente() {
        ComidaRegistro datosNuevos = new ComidaRegistro();
        datosNuevos.setFecha(LocalDate.now().minusDays(1));
        datosNuevos.setTipoComida("Cena");

        when(comidaRepository.findById(10L)).thenReturn(Optional.of(comida));
        when(comidaRepository.save(any(ComidaRegistro.class))).thenReturn(comida);

        ComidaRegistro resultado = comidaService.actualizar(10L, datosNuevos);

        assertEquals("Cena", resultado.getTipoComida());
        verify(comidaRepository).save(any(ComidaRegistro.class));
    }

    @Test
    @DisplayName("Actualizar una comida que no existe debe lanzar excepción")
    void actualizar_comidaNoExiste_lanzaExcepcion() {
        when(comidaRepository.findById(999L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> comidaService.actualizar(999L, new ComidaRegistro()));
        assertEquals("Comida no encontrada", ex.getMessage());
    }

    @Test
    @DisplayName("Eliminar una comida llama al repositorio correctamente")
    void eliminar_comidaValida_seElimina() {
        comidaService.eliminar(10L);

        verify(comidaRepository).deleteById(10L);
    }
}