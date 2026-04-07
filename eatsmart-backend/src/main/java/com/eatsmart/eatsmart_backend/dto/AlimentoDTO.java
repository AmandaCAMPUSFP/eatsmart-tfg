package com.eatsmart.eatsmart_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO (Data Transfer Object) para la entidad Alimento.
 * Se utiliza para transferir datos de alimentos entre las capas
 * de la aplicación sin exponer directamente la entidad JPA.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlimentoDTO {

    private Long idAlimento;
    private String nombre;
    private Double kcal100g;
    private Double proteinas100g;
    private Double carbohidratos100g;
    private Double grasas100g;
}
