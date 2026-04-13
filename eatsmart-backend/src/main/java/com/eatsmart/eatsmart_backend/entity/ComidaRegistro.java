package com.eatsmart.eatsmart_backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "COMIDA_REGISTRO")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ComidaRegistro {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_comida")
    @SequenceGenerator(name = "seq_comida", sequenceName = "SEQ_COMIDA_REGISTRO", allocationSize = 1)
    @Column(name = "id_comida")
    private Long idComida;

    @Column(name = "fecha", nullable = false)
    private LocalDate fecha;

    @Column(name = "tipo_comida")
    private String tipoComida;

    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;

    // ===== RELACIONES JPA =====
    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @ManyToMany
    @JoinTable(
            name = "INCLUYE_ALIMENTO",
            joinColumns = @JoinColumn(name = "id_comida"),
            inverseJoinColumns = @JoinColumn(name = "id_alimento")
    )
    private List<Alimento> alimentos = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "INCLUYE_RECETA",
            joinColumns = @JoinColumn(name = "id_comida"),
            inverseJoinColumns = @JoinColumn(name = "id_receta")
    )
    private List<Receta> recetas = new ArrayList<>();
}