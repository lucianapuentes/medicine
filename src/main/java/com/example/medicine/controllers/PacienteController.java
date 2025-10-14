package com.example.medicine.controllers;
import lombok.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.example.medicine.services.PacienteServicio;
import org.springframework.web.multipart.MultipartFile;


@RequestMapping("/pacientes")
@RequiredArgsConstructor
public class PacienteController {
    @Autowired
    private PacienteServicio pacienteServicio;


}
