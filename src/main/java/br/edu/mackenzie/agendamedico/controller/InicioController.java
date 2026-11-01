package br.edu.mackenzie.agendamedico.controller;

import org.springframework.stereotype.Controller;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class InicioController {

    @GetMapping("/")
    public String inicio() {
        return "redirect:/login";
    }

    @RequestMapping("/acesso-negado")
    public String acessoNegado(Authentication authentication, Model model) {
        boolean medico = authentication != null && authentication.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_MEDICO"));
        model.addAttribute("inicio", authentication == null ? "/login" : medico ? "/medico/painel" : "/paciente/inicio");
        return "acesso-negado";
    }

    @GetMapping("/paciente/inicio")
    public String inicioPaciente() {
        return "paciente/inicio";
    }

}
