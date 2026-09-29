package br.edu.mackenzie.agendamedico.controller;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.edu.mackenzie.agendamedico.dto.CadastroDTO;
import br.edu.mackenzie.agendamedico.service.CadastroService;
import jakarta.validation.Valid;

@Controller
public class AutenticacaoController {

    private final CadastroService cadastroService;

    public AutenticacaoController(CadastroService cadastroService) {
        this.cadastroService = cadastroService;
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/cadastro")
    public String formularioCadastro(@ModelAttribute("cadastro") CadastroDTO cadastro) {
        return "cadastro";
    }

    @PostMapping("/cadastro")
    public String cadastrar(@Valid @ModelAttribute("cadastro") CadastroDTO cadastro,
                            BindingResult resultado, RedirectAttributes redirectAttributes) {
        if (resultado.hasErrors()) {
            return "cadastro";
        }

        try {
            cadastroService.cadastrar(cadastro);
        } catch (IllegalArgumentException exception) {
            resultado.reject("cadastro", exception.getMessage());
            return "cadastro";
        }

        redirectAttributes.addFlashAttribute("sucesso", "Cadastro realizado. Entre com seu e-mail e senha.");
        return "redirect:/login";
    }
}
