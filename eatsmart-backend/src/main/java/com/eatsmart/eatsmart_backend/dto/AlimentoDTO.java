package com.eatsmart.eatsmart_backend.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlimentoDTO {

    private Long idAlimento;

    @NotBlank(message = "El nombre del alimento es obligatorio")
    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    private String nombre;

    @NotNull(message = "Las calorías por 100g son obligatorias")
    @DecimalMin(value = "0.0", inclusive = false, message = "Las calorías deben ser mayor a 0")
    @DecimalMax(value = "999.99", message = "Las calorías no pueden exceder 999.99")
    private Double kcal100g;

    @NotNull(message = "Las proteínas por 100g son obligatorias")
    @DecimalMin(value = "0.0", message = "Las proteínas no pueden ser negativas")
    @DecimalMax(value = "100.0", message = "Las proteínas no pueden exceder 100g")
    private Double proteinas100g;

    @NotNull(message = "Los carbohidratos por 100g son obligatorios")
    @DecimalMin(value = "0.0", message = "Los carbohidratos no pueden ser negativos")
    @DecimalMax(value = "100.0", message = "Los carbohidratos no pueden exceder 100g")
    private Double carbohidratos100g;

    @NotNull(message = "Las grasas por 100g son obligatorias")
    @DecimalMin(value = "0.0", message = "Las grasas no pueden ser negativas")
    @DecimalMax(value = "100.0", message = "Las grasas no pueden exceder 100g")
    private Double grasas100g;
}