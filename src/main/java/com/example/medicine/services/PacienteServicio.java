package com.example.medicine.services;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.beans.factory.annotation.Autowired;

import com.example.medicine.errors.ErrorServicio;
import com.example.medicine.model.FotoPaciente;
import com.example.medicine.model.Paciente;
import com.example.medicine.model.Usuario;
import com.example.medicine.repositories.PacienteRepositorio;
import com.example.medicine.repositories.UsuarioRepositorio;
import java.util.Optional;
import java.util.List;
@Service
@Transactional
public class PacienteServicio extends EntityServiceTemplate<Paciente> {
    @Autowired
    private PacienteRepositorio pacienteRepositorio;
    
    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @Autowired
    private FotoPacienteServicio fotoPacienteServicio;

    @Transactional
    @Override
    protected void validar(Paciente paciente) throws ErrorServicio {
    if (paciente.getNombre() == null || paciente.getNombre().isEmpty()) {
      throw new ErrorServicio("El nombre del paciente es obligatorio.");
    }
    if (paciente.getApellido() == null || paciente.getApellido().isEmpty()) {
      throw new ErrorServicio("El apellido del paciente es obligatorio.");
    }
    if (paciente.getDocumento() == null || paciente.getDocumento().isEmpty()) {
      throw new ErrorServicio("El documento del paciente es obligatorio.");
    }
    if (paciente.getDocumento().length() < 7 || paciente.getDocumento().length() > 8) {
      throw new ErrorServicio("El documento del paciente debe tener entre 7 y 8 dígitos");
    }

    if (!paciente.getDocumento().matches("\\d+")) {
      throw new ErrorServicio("El documento del paciente debe contener solo números.");
    }

    if(paciente.getFoto() == null){
      throw new ErrorServicio("El paciente debe tener una foto.");
    }
  }
  @Transactional
  @Override
    protected void guardar(Paciente paciente) throws ErrorServicio {
        System.out.println("Guardando paciente: " + paciente.getNombreCompleto());
        pacienteRepositorio.save(paciente);
    }
  
    @Transactional
    @Override
    public void actualizar(Paciente paciente) throws ErrorServicio {
      System.out.println("Actualizando paciente: " + paciente.getNombreCompleto());
      Optional<Paciente> existente = pacienteRepositorio.findById(paciente.getId());
        if (existente.isPresent()) {
            Paciente existentePaciente = existente.get();

            // Actualizamos solo los campos que se pueden modificar
            existentePaciente.setNombre(paciente.getNombre());
            existentePaciente.setApellido(paciente.getApellido());
            existentePaciente.setDocumento(paciente.getDocumento());
             // 🔹 Si se subió una nueva foto
          if (paciente.getFoto() != null ) {
              FotoPaciente fotoExistente = existentePaciente.getFoto();
              fotoExistente.setEliminado(true);
              FotoPaciente nuevaFoto = paciente.getFoto();
              nuevaFoto.setPaciente(existentePaciente); // establecer la relación
              existentePaciente.setFoto(nuevaFoto); // usa la lógica del modelo
          }

            pacienteRepositorio.save(existentePaciente);
        } else {
            throw new ErrorServicio("No se encontró el paciente para actualizar");
        }
    }

    @Override
    public void eliminar(Paciente paciente) throws ErrorServicio {
        paciente.setEliminado(true);
        System.out.println("Paciente eliminado lógicamente: " + paciente.getNombreCompleto());
        // pacienteRepositorio.save(paciente);
    }

    @Transactional
    public Optional<Paciente> findById(String id) {
        return pacienteRepositorio.findById(id);
    }

    @Transactional
    public List<Paciente> listarTodos() {
        return pacienteRepositorio.findAll();
    }
}
