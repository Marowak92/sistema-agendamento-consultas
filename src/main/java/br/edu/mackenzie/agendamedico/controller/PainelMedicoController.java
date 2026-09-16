package br.edu.mackenzie.agendamedico.controller;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import br.edu.mackenzie.agendamedico.service.PainelMedicoService;

@Controller
@RequestMapping("/medico/painel")
public class PainelMedicoController {

    private final PainelMedicoService painelMedicoService;

    public PainelMedicoController(PainelMedicoService painelMedicoService) {
        this.painelMedicoService = painelMedicoService;
    }

    @GetMapping
    public String exibirPainel(@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data,
                               Authentication authentication, Model model) {
        LocalDate dataSelecionada = data == null ? LocalDate.now() : data;
        model.addAttribute("painel", painelMedicoService.montarPainel(authentication.getName(), dataSelecionada));
        return "medico/painel";
    }
}
