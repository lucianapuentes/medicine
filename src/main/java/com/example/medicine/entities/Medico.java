package com.example.medicine.entities;
import lombok.*;
import jakarta.persistence.*;
import java.io.Serializable;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Entity
@Table(name = "medico")
public class Medico implements Serializable {

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

  public String getNombreCompleto() {
    return nombre + " " + apellido;
  }

  public boolean isActivo() {
    return !eliminado;
  }
}

