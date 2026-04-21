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
    private boolean exito;

    public AuthResponse(String mensaje, boolean exito) {
        this.mensaje = mensaje;
        this.exito = exito;
    }
}