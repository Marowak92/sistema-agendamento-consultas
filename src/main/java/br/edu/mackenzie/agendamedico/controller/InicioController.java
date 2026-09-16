package br.edu.mackenzie.agendamedico.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class InicioController {

    @GetMapping("/")
    public String inicio() {
        return "redirect:/login";
    }

    @GetMapping("/paciente/inicio")
    public String inicioPaciente() {
        return "paciente/inicio";
    }

}
