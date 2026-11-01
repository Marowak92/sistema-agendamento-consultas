package br.edu.mackenzie.agendamedico.controller;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import br.edu.mackenzie.agendamedico.repository.EmailEnviadoRepository;

/** Caixa de saida dos e-mails, so no perfil de desenvolvimento, onde nao ha servidor SMTP configurado. */
@Controller
@Profile("dev")
public class CaixaSaidaController {

    private final EmailEnviadoRepository emailRepository;

    public CaixaSaidaController(EmailEnviadoRepository emailRepository) {
        this.emailRepository = emailRepository;
    }

    @GetMapping("/dev/caixa-de-saida")
    public String listar(Model model) {
        model.addAttribute("emails", emailRepository.findAllByOrderByIdDesc());
        return "caixa-de-saida";
    }
}
