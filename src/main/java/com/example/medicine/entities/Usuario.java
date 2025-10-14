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
@Table(name = "usuario")
public class Usuario implements Serializable {

  @Id
  @GeneratedValue(generator = "UUID")
  @Column(updatable = false, nullable = false)
  private String id;

  @Column(name = "nombre_usuario")
  private String nombreUsuario;

  private String clave;

  private boolean eliminado;

  public boolean isActivo() {
    return !eliminado;
  }
}

