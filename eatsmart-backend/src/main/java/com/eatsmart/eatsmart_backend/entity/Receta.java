package com.eatsmart.eatsmart_backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "RECETA")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Receta {

    @Id
    @Column(name = "id_receta")
    private Long idReceta;

    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "descripcion")
    private String descripcion;

    @Column(name = "raciones")
    private Integer raciones;

    @Column(name = "total_kcal")
    private Double totalKcal;

    @Column(name = "total_proteinas")
    private Double totalProteinas;

    @Column(name = "total_carbohidratos")
    private Double totalCarbohidratos;

    @Column(name = "total_grasas")
    private Double totalGrasas;

    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;
}