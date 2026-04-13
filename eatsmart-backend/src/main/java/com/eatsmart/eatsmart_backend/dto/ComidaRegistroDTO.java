package com.eatsmart.eatsmart_backend.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ComidaRegistroDTO {

    private Long idComida;

    @NotNull(message = "La fecha no puede ser nula")
    @PastOrPresent(message = "La fecha debe ser en el pasado o presente")
    private LocalDate fecha;

    @NotBlank(message = "El tipo de comida no puede estar vacío")
    private String tipoComida;

    private LocalDateTime fechaCreacion;

    private Long usuarioId;

    private List<Long> alimentoIds;
    private List<Long> recetaIds;
}