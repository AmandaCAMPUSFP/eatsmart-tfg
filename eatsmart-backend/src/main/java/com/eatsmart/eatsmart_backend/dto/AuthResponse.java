package com.eatsmart.eatsmart_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {
    private String mensaje;
    private String token;
    private Long idUsuario;
    private String email;
    private Boolean exitoso;
    private String refreshToken;

    /**
     * Constructor sin refreshToken
     */
    public AuthResponse(String mensaje, String token, Long idUsuario, String email, Boolean exitoso) {
        this.mensaje = mensaje;
        this.token = token;
        this.idUsuario = idUsuario;
        this.email = email;
        this.exitoso = exitoso;
        this.refreshToken = null;
    }

    /**
     * Constructor simple
     */
    public AuthResponse(String mensaje, Boolean exitoso) {
        this.mensaje = mensaje;
        this.exitoso = exitoso;
    }
}