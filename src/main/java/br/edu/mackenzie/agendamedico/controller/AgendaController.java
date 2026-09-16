package br.edu.mackenzie.agendamedico.controller;

import java.time.LocalDate;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.edu.mackenzie.agendamedico.dto.HorarioAtendimentoDTO;
import br.edu.mackenzie.agendamedico.service.AgendaService;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/medico/agenda")
public class AgendaController {

    private final AgendaService agendaService;

    public AgendaController(AgendaService agendaService) {
        this.agendaService = agendaService;
    }

    @GetMapping
    public String exibirAgenda(@RequestParam(required = false) LocalDate data, Authentication authentication, Model model) {
        LocalDate dataSelecionada = data == null ? LocalDate.now() : data;
        model.addAttribute("horario", agendaService.novoHorario(authentication.getName()));
        model.addAttribute("dataSelecionada", dataSelecionada);
        model.addAttribute("horarios", agendaService.listarHorarios(authentication.getName(), dataSelecionada));
        return "medico/agenda";
    }

    @PostMapping
    public String adicionarHorario(@Valid @ModelAttribute("horario") HorarioAtendimentoDTO horario,
                                   BindingResult resultado, Authentication authentication, Model model,
                                   RedirectAttributes redirectAttributes) {
        if (resultado.hasErrors()) {
            preencherTelaComErros(authentication.getName(), horario.getData(), model);
            return "medico/agenda";
        }

        try {
            agendaService.adicionarHorario(authentication.getName(), horario);
        } catch (IllegalArgumentException exception) {
            resultado.reject("horario", exception.getMessage());
            preencherTelaComErros(authentication.getName(), horario.getData(), model);
            return "medico/agenda";
        }

        redirectAttributes.addFlashAttribute("sucesso", "Horário de atendimento adicionado com sucesso.");
        return "redirect:/medico/agenda?data=" + horario.getData();
    }

    @PostMapping("/excluir")
    public String excluirHorario(@RequestParam Long id, @RequestParam LocalDate data,
                                 Authentication authentication, RedirectAttributes redirectAttributes) {
        try {
            agendaService.excluirHorario(authentication.getName(), id);
            redirectAttributes.addFlashAttribute("sucesso", "Horário de atendimento removido.");
        } catch (IllegalArgumentException exception) {
            redirectAttributes.addFlashAttribute("erro", exception.getMessage());
        }
        return "redirect:/medico/agenda?data=" + data;
    }

    private void preencherTelaComErros(String emailMedico, LocalDate data, Model model) {
        LocalDate dataSelecionada = data == null ? LocalDate.now() : data;
        model.addAttribute("dataSelecionada", dataSelecionada);
        model.addAttribute("horarios", agendaService.listarHorarios(emailMedico, dataSelecionada));
    }
}
