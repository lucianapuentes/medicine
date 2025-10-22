package com.example.medicine.model;
import lombok.*;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util. Date;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "detalle_historia_clinica")
public class DetalleHistoriaClinica implements Serializable {
    @Id
    @GeneratedValue(generator = "UUID")
    @Column(updatable = false, nullable = false)
    private String id;

    @ManyToOne
    @JoinColumn(name = "historia_clinica_id", nullable = false)
    private HistoriaClinica historiaClinica;

    @Column(name = "detalleHistoria", nullable = false)
    private String detalleHistoria;

    @Column(name = "fechaHistoria", nullable = false)
    private Date fechaHistoria;

    @ManyToOne
    @JoinColumn(name = "medico_id", nullable = false)
    private Medico medico;

    @Column(name = "eliminado")
    private boolean eliminado;

    // Getters and Setters
}
