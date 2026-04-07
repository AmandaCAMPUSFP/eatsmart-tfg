package com.eatsmart.eatsmart_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO (Data Transfer Object) para la entidad Usuario.
 * Se utiliza para el registro de nuevos usuarios.
 * La contraseña se recibe en texto plano y el servicio
 * se encarga de aplicar el hash antes de persistirla.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioDTO {

    private Long idUsuario;
    private String email;
    private String contrasena;
}
