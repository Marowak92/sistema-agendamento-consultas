package br.edu.mackenzie.agendamedico.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.edu.mackenzie.agendamedico.service.ConsultaService;

@Controller
@RequestMapping("/medico/consultas")
public class ConsultaMedicoController {

    private final ConsultaService consultaService;

    public ConsultaMedicoController(ConsultaService consultaService) {
        this.consultaService = consultaService;
    }

    @GetMapping
    public String listarConsultas(Authentication authentication, Model model) {
        model.addAttribute("consultas", consultaService.listarConsultasMedico(authentication.getName()));
        return "medico/consultas";
    }

    @GetMapping("/{id}/cancelar")
    public String confirmarCancelamento(@PathVariable Long id, Authentication authentication,
                                        Model model, RedirectAttributes redirectAttributes) {
        try {
            model.addAttribute("consulta", consultaService.obterConsultaMedico(authentication.getName(), id));
            model.addAttribute("acao", "/medico/consultas/" + id + "/cancelar");
            model.addAttribute("voltar", "/medico/consultas");
            return "confirmar-cancelamento";
        } catch (IllegalArgumentException exception) {
            redirectAttributes.addFlashAttribute("erro", exception.getMessage());
            return "redirect:/medico/consultas";
        }
    }

    @PostMapping("/{id}/cancelar")
    public String cancelar(@PathVariable Long id, Authentication authentication,
                           RedirectAttributes redirectAttributes) {
        try {
            consultaService.cancelarPorMedico(authentication.getName(), id);
            redirectAttributes.addFlashAttribute("sucesso", "Consulta cancelada com sucesso.");
        } catch (IllegalArgumentException exception) {
            redirectAttributes.addFlashAttribute("erro", exception.getMessage());
        }
        return "redirect:/medico/consultas";
    }
}
