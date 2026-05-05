package com.eatsmart.eatsmart_backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "PERFIL_NUTRICIONAL")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PerfilNutricional {

    @Id
    @Column(name = "id_usuario")
    private Long idUsuario;

    @Column(name = "sexo")
    private String sexo;

    @Column(name = "fecha_nacimiento")
    private LocalDate fechaNacimiento;

    @Column(name = "altura_cm")
    private Integer alturaCm;

    @Column(name = "peso_kg")
    private Double pesoKg;

    @Column(name = "nivel_actividad")
    private String nivelActividad;

    @Column(name = "objetivo")
    private String objetivo;

    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;

    // ===== RELACIÓN JPA =====
    // Relación 1:1 con Usuario, comparte la misma clave primaria (id_usuario).
    // Se usa @JsonIgnore para evitar el bucle infinito al serializar a JSON
    // (Usuario -> Perfil -> Usuario -> Perfil...).
    @OneToOne
    @JoinColumn(name = "id_usuario", insertable = false, updatable = false)
    @JsonIgnore
    private Usuario usuario;

    // ===== LÓGICA DE NEGOCIO =====
    /**
     * Calcula el índice de masa corporal (IMC)
     */
    public Double calcularIMC() {
        if (alturaCm == null || pesoKg == null || alturaCm <= 0) {
            return null;
        }
        double alturaMetros = alturaCm / 100.0;
        return pesoKg / (alturaMetros * alturaMetros);
    }

    /**
     * Calcula la edad del usuario teniendo en cuenta mes y día.
     */
    public Integer calcularEdad() {
        if (fechaNacimiento == null) {
            return null;
        }
        LocalDate hoy = LocalDate.now();
        int edad = hoy.getYear() - fechaNacimiento.getYear();
        if (hoy.getMonthValue() < fechaNacimiento.getMonthValue() ||
                (hoy.getMonthValue() == fechaNacimiento.getMonthValue() &&
                        hoy.getDayOfMonth() < fechaNacimiento.getDayOfMonth())) {
            edad--;
        }
        return edad;
    }

    /**
     * Calcula el metabolismo basal (TMB) usando la fórmula de Harris-Benedict
     */
    public Double calcularMetabolismoBasal() {
        if (pesoKg == null || alturaCm == null || fechaNacimiento == null) {
            return null;
        }
        Integer edad = calcularEdad();

        if ("Hombre".equalsIgnoreCase(sexo)) {
            return 88.362 + (13.397 * pesoKg) + (4.799 * alturaCm) - (5.677 * edad);
        } else {
            return 447.593 + (9.247 * pesoKg) + (3.098 * alturaCm) - (4.330 * edad);
        }
    }

    /**
     * Calcula el gasto calórico total diario (TDEE)
     */
    public Double calcularGastoCaloricoTotal() {
        Double tmb = calcularMetabolismoBasal();
        if (tmb == null || nivelActividad == null) {
            return null;
        }

        double factorActividad = switch (nivelActividad.toLowerCase()) {
            case "sedentario" -> 1.2;
            case "ligero" -> 1.375;
            case "moderado" -> 1.55;
            case "intenso" -> 1.725;
            case "muy intenso" -> 1.9;
            default -> 1.55;
        };

        return tmb * factorActividad;
    }

    /**
     * Calcula el objetivo calórico diario en base al objetivo nutricional
     */
    public Double calcularObjetivoCaloricodiario() {
        Double tdee = calcularGastoCaloricoTotal();
        if (tdee == null || objetivo == null) {
            return null;
        }

        return switch (objetivo.toLowerCase()) {
            case "pérdida de peso" -> tdee - 500;
            case "mantenimiento" -> tdee;
            case "ganancia muscular" -> tdee + 300;
            default -> tdee;
        };
    }

    /**
     * Calcula la distribución de macronutrientes en base al objetivo
     */
    public double[] calcularDistribucionMacros() {
        if (objetivo == null) {
            return new double[]{30, 40, 30};
        }

        return switch (objetivo.toLowerCase()) {
            case "pérdida de peso" -> new double[]{35, 35, 30};
            case "ganancia muscular" -> new double[]{30, 40, 30};
            case "mantenimiento" -> new double[]{25, 50, 25};
            default -> new double[]{30, 40, 30};
        };
    }
}