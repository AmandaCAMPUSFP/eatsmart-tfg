package com.eatsmart.eatsmart_backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "PERFIL_NUTRICIONAL")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PerfilNutricional {

    @Id
    @Column(name = "id_usuario")
    private Long idUsuario;

    @Column(name = "sexo")
    private String sexo;

    @Column(name = "fecha_nacimiento")
    private LocalDate fechaNacimiento;

    @Column(name = "altura_cm")
    private Integer alturaCm;

    @Column(name = "peso_kg")
    private Double pesoKg;

    @Column(name = "nivel_actividad")
    private String nivelActividad;

    @Column(name = "objetivo")
    private String objetivo;

    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;
}