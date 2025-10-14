package com.example.medicine.entities;

import lombok.*;
import jakarta.persistence.*;
import java.io.Serializable;
import com.example.medicine.entities.FotoPaciente;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Entity
@Table(name = "paciente")
public class Paciente implements Serializable {
  @Id
  @GeneratedValue(generator = "UUID")
  @Column(updatable = false, nullable = false)
  private String id;

  @Column(name = "nombre")
  private String nombre;

  @Column(name = "apellido")
  private String apellido;

  @Column(name = "documento")
  private String documento;

  @Column(name = "eliminado")
  private boolean eliminado;

  @OneToMany(mappedBy = "paciente", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  private List<FotoPaciente> fotos;

  public String getNombreCompleto() {
    return nombre + " " + apellido;
  }

  public boolean isActivo() {
    return !eliminado;
  }

  public void agregarFoto(FotoPaciente foto) {
    if (fotos == null) {
      fotos = new ArrayList<>();
    } else {
        for (FotoPaciente f : fotos) {
            if (!f.isEliminado()) {
                f.setEliminado(true);
            }
        }
    }
    fotos.add(foto);
    foto.setPaciente(this);
  }

  public Optional<FotoPaciente> mostrarFoto(List<FotoPaciente> fotos) {
    for (FotoPaciente f : fotos) {
        if (!f.isEliminado()) {
            return Optional.of(f);
        }
    }
    return Optional.empty();
    }
}

