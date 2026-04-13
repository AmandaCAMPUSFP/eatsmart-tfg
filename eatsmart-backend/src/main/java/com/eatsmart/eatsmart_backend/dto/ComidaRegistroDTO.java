package com.eatsmart.eatsmart_backend.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ComidaRegistroDTO {

    private Long idComida;

    @NotNull(message = "El usuario es obligatorio")
    private Long idUsuario;

    @NotNull(message = "La fecha es obligatoria")
    @PastOrPresent(message = "La fecha no puede ser futura")
    private LocalDate fecha;

    @NotBlank(message = "El tipo de comida es obligatorio")
    @Pattern(regexp = "^(Desayuno|Almuerzo|Cena|Snack)$",
            message = "El tipo debe ser: Desayuno, Almuerzo, Cena o Snack")
    private String tipoComida;

    private List<Long> idAlimentos; // IDs de alimentos
    private List<Long> idRecetas;   // IDs de recetas
}