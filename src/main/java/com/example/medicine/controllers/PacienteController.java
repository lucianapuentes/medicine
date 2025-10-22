package com.example.medicine.controllers;
import lombok.*;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import com.example.medicine.services.PacienteServicio;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.ui.Model;
import com.example.medicine.model.Paciente;
import com.example.medicine.model.FotoPaciente;
import java.util.ArrayList;
import java.util.Optional;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
@Controller
@RequestMapping("/pacientes")
@RequiredArgsConstructor
public class PacienteController {
    @Autowired
    private PacienteServicio pacienteServicio;


    @PatchMapping("/modificar/{id}")
    public String modificarPaciente(
            @PathVariable String id,
            @RequestParam String nombre,
            @RequestParam String apellido,
            @RequestParam String documento,
            @RequestParam MultipartFile archivo,
            Model model
    ) throws Exception {
        Optional <Paciente> pacienteOpt = pacienteServicio.findById(id);
        if (!pacienteOpt.isPresent()) {
            throw new Exception("No se encontró el paciente solicitado");
        }
        Paciente paciente = pacienteOpt.get();
        paciente.setNombre(nombre);
        paciente.setApellido(apellido);
        paciente.setDocumento(documento);
        if (archivo != null && !archivo.isEmpty()) {
            FotoPaciente fotoExistente = paciente.getFoto();
            if (fotoExistente != null) {
                fotoExistente.setMime(archivo.getContentType());
                fotoExistente.setNombre(archivo.getName());
                fotoExistente.setContenido(archivo.getBytes());
            } else {
                FotoPaciente nuevaFoto = new FotoPaciente();
                nuevaFoto.setMime(archivo.getContentType());
                nuevaFoto.setNombre(archivo.getName());
                nuevaFoto.setContenido(archivo.getBytes());
                paciente.setFoto(nuevaFoto);
            }
        }
        pacienteServicio.actualizar(paciente);
        return "redirect:/pacientes/listar";
    }
    @PostMapping("/crear")
    public String crearPaciente(
        @ModelAttribute("nombre") String nombre,
        @ModelAttribute("apellido") String apellido,
        @ModelAttribute("documento") String documento,
        @ModelAttribute("archivo") MultipartFile foto, // Esto es el archivo
        Model model
        ) throws Exception {
        Paciente paciente = new Paciente();
        paciente.setNombre(nombre);
        paciente.setApellido(apellido);
        paciente.setDocumento(documento);
        
        // --- Lógica de Corrección ---
        
        if (foto != null) { 
            FotoPaciente fotoPaciente = new FotoPaciente();
            
            // CORRECCIÓN 1: Manejar el tipo MIME nulo
            String mimeType = foto.getContentType();
            fotoPaciente.setMime(mimeType != null ? mimeType : "application/octet-stream");
            
            // CORRECCIÓN 2: Manejar el nombre del archivo nulo o vacío
            String fileName = foto.getOriginalFilename();
            fotoPaciente.setNombre(fileName != null && !fileName.isEmpty() ? fileName : "archivo-sin-nombre");
            fotoPaciente.setPaciente(paciente);
            
            // Obtener el contenido del archivo
            fotoPaciente.setContenido(foto.getBytes());
            
            // Importante: Si Paciente tiene una lista de fotos, usa .getFotos().add(fotoPaciente);
            // Si Paciente tiene un solo campo de foto:
            paciente.setFoto(fotoPaciente);
        } else {
            // Opción A: Si la foto es OBLIGATORIA:
             model.addAttribute("error", "La foto es obligatoria.");
             return "views/pacientes/formPaciente"; 

            // Opción B: Si la foto es OPCIONAL, simplemente no se añade nada.
        }
        
        // ----------------------------

        pacienteServicio.crear(paciente);
        return "redirect:/pacientes/listar";
    }

    @PostMapping("/eliminar/{id}")
    public String eliminarPaciente(@PathVariable String id, Model model) throws Exception {
        Optional <Paciente> pacienteOpt = pacienteServicio.findById(id);
        if (!pacienteOpt.isPresent()) {
            throw new Exception("No se encontró el paciente solicitado");
        }
        Paciente paciente = pacienteOpt.get();
        pacienteServicio.eliminar(paciente);
        return "redirect:views/pacientes";
    }

    @GetMapping("")
    public String listarPacientes(Model model) {
        // La lista de pacientes se añade al modelo bajo el nombre "pacientes"
        List<Paciente> pacientes = pacienteServicio.listarTodos();
        model.addAttribute("pacientes", pacientes);
        
        // Retorna el nombre del archivo HTML de la plantilla (ej. src/main/resources/templates/listaPacientes.html)
       return "views/pacientes";
    }
    @GetMapping("/nuevo")
    public String mostrarFormularioCreacion(Model model) {
        // Añadir un objeto Paciente vacío es opcional, pero buena práctica
        // model.addAttribute("paciente", new Paciente()); 
        return "views/formPaciente"; // Ruta a formulario.html
    }
    @GetMapping("/foto/{id}")
    public ResponseEntity<byte[]> obtenerFoto(@PathVariable String id) {
        Optional<Paciente> pacienteOpt = pacienteServicio.findById(id);

        if (pacienteOpt.isPresent()) {
            // ACCESO DIRECTO a la única foto
            FotoPaciente foto = pacienteOpt.get().getFoto(); 

            if (foto != null && foto.getContenido() != null) {
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.valueOf(foto.getMime()));
                
                return new ResponseEntity<>(foto.getContenido(), headers, HttpStatus.OK);
            }
        }

        // Retorna 404 si el paciente o la foto no existe
        return new ResponseEntity<>(HttpStatus.NOT_FOUND); 
    }
  

}
