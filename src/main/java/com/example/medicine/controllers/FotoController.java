package com.example.medicine.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;
import com.example.medicine.services.PacienteServicio;
import com.example.medicine.model.Paciente;
import com.example.medicine.model.FotoPaciente;

@Controller
@RequestMapping("/foto")
public class FotoController {
    @Autowired
    private PacienteServicio pacienteServicio;
     @GetMapping("/paciente/{id}")
    @ResponseBody
    public ResponseEntity<byte[]> fotoPaciente(@PathVariable String id) {
        try {
            Paciente paciente = pacienteServicio.findById(id).get();

            if (paciente.getFoto() == null) {
                return ResponseEntity.notFound().build();
            }

            FotoPaciente foto = paciente.getFoto();

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_TYPE, foto.getMime())
                    .body(foto.getContenido());

        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}