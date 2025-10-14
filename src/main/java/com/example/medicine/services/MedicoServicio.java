package com.example.medicine.services;
import com.example.medicine.entities.Medico;
import com.example.medicine.errors.ErrorServicio;
import com.example.medicine.repositories.MedicoRepositorio;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class MedicoServicio {

    @Autowired
    private MedicoRepositorio medicoRepositorio;

    // Busqueda

    @Transactional
    public List<Medico> listarMedicos() throws ErrorServicio {
        try {
            return medicoRepositorio.findAll();
        } catch (Exception e) {
            throw new ErrorServicio("Error del sistema");
        }
    }

    @Transactional
    public Medico buscarMedico(String id) throws ErrorServicio {
        try {
            Optional<Medico> opt = medicoRepositorio.findById(id);
            if (opt.isPresent()) {
                return opt.get();
            } else {
                throw new ErrorServicio("No se encontro el medico solicitado");
            }
        } catch (Exception e) {
            throw new ErrorServicio("Error del sistema");
        }
    }

    // Escritura

    @Transactional
    public void crearMedico(Medico medico) throws ErrorServicio {
        try {
            medico.setEliminado(false);
            medicoRepositorio.save(medico);
        } catch (Exception e) {
            throw new ErrorServicio("Error del sistema");
        }
    }

    @Transactional
    public void modificarMedico(String id, String nombre, String apellido, String documento) throws ErrorServicio {
        try {
            Medico medico = buscarMedico(id);
            if (medico == null) {
                throw new ErrorServicio("No se encontro el medico solicitado");
            }
            medico.setNombre(nombre);
            medico.setApellido(apellido);
            medico.setDocumento(documento);
            medicoRepositorio.save(medico);
        } catch (Exception e) {
            throw new ErrorServicio("Error del sistema");
        }
    }

    // Eliminacion

    @Transactional
    public void eliminarMedico(String id) throws ErrorServicio {
        try {
            Medico medico = buscarMedico(id);
            if (medico == null) {
                throw new ErrorServicio("No se encontro el medico solicitado");
            }
            medico.setEliminado(true);
            medicoRepositorio.save(medico);
        } catch (Exception e) {
            throw new ErrorServicio("Error del sistema");
        }
    }
}
