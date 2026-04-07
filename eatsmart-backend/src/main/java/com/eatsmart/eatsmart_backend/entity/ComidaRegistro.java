package com.eatsmart.eatsmart_backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "COMIDA_REGISTRO")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ComidaRegistro {

    @Id
    @Column(name = "id_comida")
    private Long idComida;

    @Column(name = "id_usuario", nullable = false)
    private Long idUsuario;

    @Column(name = "fecha", nullable = false)
    private LocalDate fecha;

    @Column(name = "tipo_comida")
    private String tipoComida;

    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;
}