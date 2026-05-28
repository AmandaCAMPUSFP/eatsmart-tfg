package com.eatsmart.eatsmart_backend;

import com.eatsmart.eatsmart_backend.entity.Usuario;
import com.eatsmart.eatsmart_backend.repository.UsuarioRepository;
import com.eatsmart.eatsmart_backend.service.PasswordValidationService;
import com.eatsmart.eatsmart_backend.service.UsuarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests del servicio de usuarios")
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private PasswordValidationService passwordValidationService;

    @InjectMocks
    private UsuarioService usuarioService;

    private Usuario usuario;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setIdUsuario(1L);
        usuario.setEmail("test@eatsmart.com");
        usuario.setContrasenaHash("Password123!");
    }

    @Test
    @DisplayName("Registrar un usuario nuevo debe encriptar la contraseña y guardarlo")
    void registrar_usuarioNuevo_seGuardaCorrectamente() {
        // Arrange
        when(usuarioRepository.findByEmail("test@eatsmart.com")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("Password123!")).thenReturn("$2a$10$hashFicticio");
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        // Act
        Usuario resultado = usuarioService.registrar(usuario);

        // Assert
        assertNotNull(resultado);
        verify(passwordValidationService).validarContraseña("Password123!");
        verify(passwordEncoder).encode("Password123!");
        verify(usuarioRepository).save(any(Usuario.class));
    }

    @Test
    @DisplayName("Registrar con email duplicado debe lanzar excepción")
    void registrar_emailDuplicado_lanzaExcepcion() {
        // Arrange
        when(usuarioRepository.findByEmail("test@eatsmart.com")).thenReturn(Optional.of(usuario));

        // Act + Assert
        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> usuarioService.registrar(usuario));
        assertEquals("El email ya está registrado", ex.getMessage());
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    @Test
    @DisplayName("Registrar con contraseña débil debe lanzar excepción")
    void registrar_contrasenaDebil_lanzaExcepcion() {
        // Arrange
        when(usuarioRepository.findByEmail("test@eatsmart.com")).thenReturn(Optional.empty());
        doThrow(new IllegalArgumentException("Contraseña demasiado débil"))
                .when(passwordValidationService).validarContraseña("Password123!");

        // Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> usuarioService.registrar(usuario));
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    @Test
    @DisplayName("Autenticar con credenciales correctas debe devolver el usuario")
    void autenticar_credencialesCorrectas_devuelveUsuario() {
        // Arrange
        usuario.setContrasenaHash("$2a$10$hashFicticio");
        when(usuarioRepository.findByEmail("test@eatsmart.com")).thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches("Password123!", "$2a$10$hashFicticio")).thenReturn(true);

        // Act
        Usuario resultado = usuarioService.autenticar("test@eatsmart.com", "Password123!");

        // Assert
        assertNotNull(resultado);
        assertEquals("test@eatsmart.com", resultado.getEmail());
    }

    @Test
    @DisplayName("Autenticar con email no registrado debe lanzar excepción")
    void autenticar_emailNoRegistrado_lanzaExcepcion() {
        // Arrange
        when(usuarioRepository.findByEmail("noexiste@eatsmart.com")).thenReturn(Optional.empty());

        // Act + Assert
        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> usuarioService.autenticar("noexiste@eatsmart.com", "Password123!"));
        assertEquals("Email o contraseña incorrectos", ex.getMessage());
    }

    @Test
    @DisplayName("Autenticar con contraseña incorrecta debe lanzar excepción")
    void autenticar_contrasenaIncorrecta_lanzaExcepcion() {
        // Arrange
        usuario.setContrasenaHash("$2a$10$hashFicticio");
        when(usuarioRepository.findByEmail("test@eatsmart.com")).thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches("ContrasenaMala", "$2a$10$hashFicticio")).thenReturn(false);

        // Act + Assert
        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> usuarioService.autenticar("test@eatsmart.com", "ContrasenaMala"));
        assertEquals("Email o contraseña incorrectos", ex.getMessage());
    }

    @Test
    @DisplayName("Obtener por email debe devolver el usuario si existe")
    void obtenerPorEmail_usuarioExiste_devuelveUsuario() {
        // Arrange
        when(usuarioRepository.findByEmail("test@eatsmart.com")).thenReturn(Optional.of(usuario));

        // Act
        Optional<Usuario> resultado = usuarioService.obtenerPorEmail("test@eatsmart.com");

        // Assert
        assertTrue(resultado.isPresent());
        assertEquals("test@eatsmart.com", resultado.get().getEmail());
    }
}