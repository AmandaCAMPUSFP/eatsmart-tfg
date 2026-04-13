package com.eatsmart.eatsmart_backend.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecetaDTO {

    private Long idReceta;

    @NotBlank(message = "El nombre de la receta no puede estar vacío")
    @Size(min = 2, max = 150, message = "El nombre debe tener entre 2 y 150 caracteres")
    private String nombre;

    private String descripcion;

    @NotNull(message = "Las raciones no pueden ser nulas")
    @Min(value = 1, message = "Las raciones deben ser mayor a 0")
    private Integer raciones;

    @NotNull(message = "Las calorías totales no pueden ser nulas")
    @DecimalMin(value = "0.0", message = "Las calorías deben ser mayor o igual a 0")
    private Double totalKcal;

    @NotNull(message = "Las proteínas totales no pueden ser nulas")
    @DecimalMin(value = "0.0", message = "Las proteínas deben ser mayor o igual a 0")
    private Double totalProteinas;

    @NotNull(message = "Los carbohidratos totales no pueden ser nulos")
    @DecimalMin(value = "0.0", message = "Los carbohidratos deben ser mayor o igual a 0")
    private Double totalCarbohidratos;

    @NotNull(message = "Las grasas totales no pueden ser nulas")
    @DecimalMin(value = "0.0", message = "Las grasas deben ser mayor o igual a 0")
    private Double totalGrasas;

    private LocalDateTime fechaCreacion;

    private List<Long> alimentoIds;
}