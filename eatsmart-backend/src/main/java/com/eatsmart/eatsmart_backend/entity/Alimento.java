package com.eatsmart.eatsmart_backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "ALIMENTO")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Alimento {

    @Id
    @Column(name = "id_alimento")
    private Long idAlimento;

    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "kcal_100g")
    private Double kcal100g;

    @Column(name = "proteinas_100g")
    private Double proteinas100g;

    @Column(name = "carbohidratos_100g")
    private Double carbohidratos100g;

    @Column(name = "grasas_100g")
    private Double grasas100g;

    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;
}