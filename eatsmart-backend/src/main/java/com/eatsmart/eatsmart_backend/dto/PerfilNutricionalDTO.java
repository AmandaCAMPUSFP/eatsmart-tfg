package com.eatsmart.eatsmart_backend.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PerfilNutricionalDTO {

    private Long idUsuario;

    @NotBlank(message = "El sexo es obligatorio")
    @Pattern(regexp = "^(Hombre|Mujer)$", message = "El sexo debe ser 'Hombre' o 'Mujer'")
    private String sexo;

    @NotNull(message = "La fecha de nacimiento es obligatoria")
    @PastOrPresent(message = "La fecha de nacimiento no puede ser futura")
    private LocalDate fechaNacimiento;

    @NotNull(message = "La altura es obligatoria")
    @Min(value = 100, message = "La altura debe ser al menos 100 cm")
    @Max(value = 250, message = "La altura no puede exceder 250 cm")
    private Integer alturaCm;

    @NotNull(message = "El peso es obligatorio")
    @DecimalMin(value = "30.0", message = "El peso debe ser al menos 30 kg")
    @DecimalMax(value = "300.0", message = "El peso no puede exceder 300 kg")
    private Double pesoKg;

    @NotBlank(message = "El nivel de actividad es obligatorio")
    @Pattern(regexp = "^(Sedentario|Ligero|Moderado|Intenso|Muy intenso)$",
            message = "El nivel debe ser: Sedentario, Ligero, Moderado, Intenso o Muy intenso")
    private String nivelActividad;

    @NotBlank(message = "El objetivo es obligatorio")
    @Pattern(regexp = "^(Pérdida de peso|Mantenimiento|Ganancia muscular)$",
            message = "El objetivo debe ser: Pérdida de peso, Mantenimiento o Ganancia muscular")
    private String objetivo;
}