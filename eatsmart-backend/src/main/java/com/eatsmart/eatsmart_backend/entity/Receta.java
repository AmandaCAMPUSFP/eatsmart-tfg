package com.eatsmart.eatsmart_backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "RECETA")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Receta {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_receta")
    @SequenceGenerator(name = "seq_receta", sequenceName = "SEQ_RECETA", allocationSize = 1)
    @Column(name = "id_receta")
    private Long idReceta;

    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "descripcion")
    private String descripcion;

    @Column(name = "raciones")
    private Integer raciones;

    @Column(name = "total_kcal")
    private Double totalKcal;

    @Column(name = "total_proteinas")
    private Double totalProteinas;

    @Column(name = "total_carbohidratos")
    private Double totalCarbohidratos;

    @Column(name = "total_grasas")
    private Double totalGrasas;

    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;

    // ===== RELACIONES JPA =====
    @ManyToMany
    @JoinTable(
            name = "COMPUESTA_POR",
            joinColumns = @JoinColumn(name = "id_receta"),
            inverseJoinColumns = @JoinColumn(name = "id_alimento")
    )
    private List<Alimento> alimentos = new ArrayList<>();

    // ===== LÓGICA DE NEGOCIO =====
    /**
     * Calcula los nutrientes por ración
     * @return array con [kcal/ración, proteínas/ración, carbs/ración, grasas/ración]
     */
    public double[] calcularNutrientesporRacion() {
        if (raciones == null || raciones <= 0) {
            return new double[]{0, 0, 0, 0};
        }

        return new double[]{
                totalKcal != null ? totalKcal / raciones : 0,
                totalProteinas != null ? totalProteinas / raciones : 0,
                totalCarbohidratos != null ? totalCarbohidratos / raciones : 0,
                totalGrasas != null ? totalGrasas / raciones : 0
        };
    }

    /**
     * Calcula nutrientes para X raciones consumidas
     * @param racionesConsumidas número de raciones
     * @return array con [kcal, proteínas, carbs, grasas]
     */
    public double[] calcularNutrientesParaRaciones(double racionesConsumidas) {
        double[] porRacion = calcularNutrientesporRacion();
        return new double[]{
                porRacion[0] * racionesConsumidas,
                porRacion[1] * racionesConsumidas,
                porRacion[2] * racionesConsumidas,
                porRacion[3] * racionesConsumidas
        };
    }
}