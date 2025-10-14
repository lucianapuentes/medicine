package com.example.medicine.services;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.medicine.entities.Paciente;
import com.example.medicine.entities.Usuario;
import com.example.medicine.errors.ErrorServicio;
import com.example.medicine.repositories.PacienteRepositorio;
import com.example.medicine.repositories.UsuarioRepositorio;
import java.util.Optional;
import com.example.medicine.entities.FotoPaciente;

@Service
@Transactional
public class PacienteServicio {
    @Autowired
    private PacienteRepositorio pacienteRepositorio;
    
    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @Autowired
    private FotoPacienteServicio fotoPacienteServicio;
    
    @Transactional
    public void crearPaciente(String idUsuario, String nombre, String apellido, String documento, MultipartFile archivo) throws ErrorServicio {

        Optional<Usuario> usuario = usuarioRepositorio.findById(idUsuario);
        if (usuario.isPresent()) {
            Paciente paciente = Paciente.builder()
                    .nombre(nombre)
                    .apellido(apellido)
                    .documento(documento)
                    .eliminado(false)
                    .build();

            FotoPaciente foto = fotoPacienteServicio.guardar(archivo, paciente);
            paciente.agregarFoto(foto);

            pacienteRepositorio.save(paciente);
        } else {
            // Manejar el caso en que el usuario no existe
            throw new ErrorServicio("Usuario no encontrado");
        }
    }
}
