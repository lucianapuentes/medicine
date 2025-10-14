package com.example.medicine.repositories;
import com.example.medicine.entities.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
@Repository
public interface PacienteRepositorio extends JpaRepository<Paciente, String> {

  @Query(value = "SELECT * FROM paciente WHERE paciente.eliminado = false", nativeQuery = true)
  List<Paciente> findAllActives();

  @Query(value = "SELECT * FROM paciente WHERE paciente.id =:id AND paciente.eliminado = false", nativeQuery = true)
  Optional<Paciente> findById(@Param("id") String id);

}
