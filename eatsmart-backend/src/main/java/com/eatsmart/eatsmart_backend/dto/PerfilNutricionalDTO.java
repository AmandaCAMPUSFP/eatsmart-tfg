package com.eatsmart.eatsmart_backend.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PerfilNutricionalDTO {

    private Long idUsuario;

    @NotBlank(message = "El sexo no puede estar vacío")
    private String sexo;

    @NotNull(message = "La fecha de nacimiento no puede ser nula")
    @PastOrPresent(message = "La fecha de nacimiento debe ser en el pasado o presente")
    private LocalDate fechaNacimiento;

    @NotNull(message = "La altura no puede ser nula")
    @Min(value = 50, message = "La altura debe ser mayor a 50 cm")
    @Max(value = 250, message = "La altura debe ser menor a 250 cm")
    private Integer alturaCm;

    @NotNull(message = "El peso no puede ser nulo")
    @DecimalMin(value = "20.0", message = "El peso debe ser mayor a 20 kg")
    @DecimalMax(value = "500.0", message = "El peso debe ser menor a 500 kg")
    private Double pesoKg;

    @NotBlank(message = "El nivel de actividad no puede estar vacío")
    private String nivelActividad;

    @NotBlank(message = "El objetivo no puede estar vacío")
    private String objetivo;

    private LocalDateTime fechaActualizacion;
}