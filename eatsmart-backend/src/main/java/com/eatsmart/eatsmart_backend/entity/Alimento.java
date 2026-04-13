package com.eatsmart.eatsmart_backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "ALIMENTO")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Alimento {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_alimento")
    @SequenceGenerator(name = "seq_alimento", sequenceName = "SEQ_ALIMENTO", allocationSize = 1)
    @Column(name = "id_alimento")
    private Long idAlimento;

    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "kcal_100g")
    private Double kcal100g;

    @Column(name = "proteinas_100g")
    private Double proteinas100g;

    @Column(name = "carbohidratos_100g")
    private Double carbohidratos100g;

    @Column(name = "grasas_100g")
    private Double grasas100g;

    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;

    // ===== LÓGICA DE NEGOCIO =====
    /**
     * Calcula los valores nutricionales para una cantidad específica en gramos
     * @param gramos cantidad del alimento
     * @return array con [kcal, proteínas, carbohidratos, grasas]
     */
    public double[] calcularNutrientes(double gramos) {
        if (gramos <= 0) {
            return new double[]{0, 0, 0, 0};
        }

        double factor = gramos / 100.0;
        return new double[]{
                kcal100g != null ? kcal100g * factor : 0,
                proteinas100g != null ? proteinas100g * factor : 0,
                carbohidratos100g != null ? carbohidratos100g * factor : 0,
                grasas100g != null ? grasas100g * factor : 0
        };
    }

    /**
     * Calcula solo las calorías para una cantidad en gramos
     * @param gramos cantidad del alimento
     * @return calorías
     */
    public Double calcularCaloriasParaGramos(double gramos) {
        if (gramos <= 0 || kcal100g == null) {
            return 0.0;
        }
        return kcal100g * (gramos / 100.0);
    }
}