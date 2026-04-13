package com.eatsmart.eatsmart_backend.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlimentoDTO {

    private Long idAlimento;

    @NotBlank(message = "El nombre del alimento no puede estar vacío")
    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    private String nombre;

    @NotNull(message = "Las calorías no pueden ser nulas")
    @DecimalMin(value = "0.0", message = "Las calorías deben ser mayor o igual a 0")
    private Double kcal100g;

    @NotNull(message = "Las proteínas no pueden ser nulas")
    @DecimalMin(value = "0.0", message = "Las proteínas deben ser mayor o igual a 0")
    private Double proteinas100g;

    @NotNull(message = "Los carbohidratos no pueden ser nulos")
    @DecimalMin(value = "0.0", message = "Los carbohidratos deben ser mayor o igual a 0")
    private Double carbohidratos100g;

    @NotNull(message = "Las grasas no pueden ser nulas")
    @DecimalMin(value = "0.0", message = "Las grasas deben ser mayor o igual a 0")
    private Double grasas100g;

    private LocalDateTime fechaCreacion;
}