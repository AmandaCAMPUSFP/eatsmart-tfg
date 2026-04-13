package com.eatsmart.eatsmart_backend.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecetaDTO {

    private Long idReceta;

    @NotBlank(message = "El nombre de la receta es obligatorio")
    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    private String nombre;

    @Size(max = 500, message = "La descripción no puede exceder 500 caracteres")
    private String descripcion;

    @NotNull(message = "El número de raciones es obligatorio")
    @Min(value = 1, message = "Debe haber al menos 1 ración")
    @Max(value = 20, message = "No pueden haber más de 20 raciones")
    private Integer raciones;

    @NotNull(message = "El total de calorías es obligatorio")
    @DecimalMin(value = "0.0", inclusive = false, message = "Las calorías deben ser mayor a 0")
    private Double totalKcal;

    @NotNull(message = "El total de proteínas es obligatorio")
    @DecimalMin(value = "0.0", message = "Las proteínas no pueden ser negativas")
    private Double totalProteinas;

    @NotNull(message = "El total de carbohidratos es obligatorio")
    @DecimalMin(value = "0.0", message = "Los carbohidratos no pueden ser negativos")
    private Double totalCarbohidratos;

    @NotNull(message = "El total de grasas es obligatorio")
    @DecimalMin(value = "0.0", message = "Las grasas no pueden ser negativas")
    private Double totalGrasas;

    @NotEmpty(message = "La receta debe tener al menos un alimento")
    private List<Long> idAlimentos; // IDs de alimentos
}