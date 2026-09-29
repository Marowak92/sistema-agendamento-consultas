package br.edu.mackenzie.agendamedico.controller;

import java.time.LocalDate;
import java.time.LocalTime;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.edu.mackenzie.agendamedico.dto.AgendamentoDTO;
import br.edu.mackenzie.agendamedico.service.ConsultaService;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/paciente")
public class ConsultaController {

    private final ConsultaService consultaService;

    public ConsultaController(ConsultaService consultaService) {
        this.consultaService = consultaService;
    }

    @GetMapping("/medicos")
    public String listarMedicos(Model model) {
        model.addAttribute("medicos", consultaService.listarMedicos());
        return "paciente/medicos";
    }

    @GetMapping("/agenda-medico")
    public String exibirAgendaMedico(@RequestParam Long medicoId, @RequestParam(required = false) LocalDate data,
                                     Model model, RedirectAttributes redirectAttributes) {
        LocalDate dataSelecionada = data == null ? LocalDate.now() : data;
        try {
            model.addAttribute("medico", consultaService.obterMedico(medicoId));
            model.addAttribute("dataSelecionada", dataSelecionada);
            model.addAttribute("horarios", consultaService.horariosDisponiveis(medicoId, dataSelecionada));
            return "paciente/agenda-medico";
        } catch (IllegalArgumentException exception) {
            redirectAttributes.addFlashAttribute("erro", exception.getMessage());
            return "redirect:/paciente/medicos";
        }
    }

    @GetMapping("/resumo-consulta")
    public String exibirResumo(@RequestParam Long medicoId, @RequestParam LocalDate data,
                               @RequestParam LocalTime horario, Model model, RedirectAttributes redirectAttributes) {
        AgendamentoDTO agendamento = criarAgendamento(medicoId, data, horario);
        try {
            model.addAttribute("consulta", consultaService.prepararResumo(agendamento));
            model.addAttribute("agendamento", agendamento);
            return "paciente/resumo-consulta";
        } catch (IllegalArgumentException exception) {
            redirectAttributes.addFlashAttribute("erro", exception.getMessage());
            return "redirect:/paciente/agenda-medico?medicoId=" + medicoId + "&data=" + data;
        }
    }

    @PostMapping("/consultas")
    public String confirmarConsulta(@Valid @ModelAttribute("agendamento") AgendamentoDTO agendamento,
                                    BindingResult resultado, Authentication authentication,
                                    RedirectAttributes redirectAttributes) {
        if (resultado.hasErrors()) {
            redirectAttributes.addFlashAttribute("erro", "Dados do agendamento inválidos.");
            return "redirect:/paciente/medicos";
        }
        try {
            consultaService.agendar(authentication.getName(), agendamento);
            redirectAttributes.addFlashAttribute("sucesso", "Consulta agendada com sucesso.");
            return "redirect:/paciente/medicos";
        } catch (IllegalArgumentException exception) {
            redirectAttributes.addFlashAttribute("erro", exception.getMessage());
            return "redirect:/paciente/agenda-medico?medicoId=" + agendamento.getMedicoId()
                    + "&data=" + agendamento.getData();
        }
    }

    @GetMapping("/minhas-consultas")
    public String listarConsultasPaciente(Authentication authentication, Model model) {
        model.addAttribute("consultas", consultaService.listarConsultasPaciente(authentication.getName()));
        return "paciente/minhas-consultas";
    }

    @GetMapping("/consultas/{id}/cancelar")
    public String confirmarCancelamentoPaciente(@PathVariable Long id, Authentication authentication,
                                                Model model, RedirectAttributes redirectAttributes) {
        try {
            model.addAttribute("consulta", consultaService.obterConsultaPaciente(authentication.getName(), id));
            model.addAttribute("acao", "/paciente/consultas/" + id + "/cancelar");
            model.addAttribute("voltar", "/paciente/minhas-consultas");
            return "confirmar-cancelamento";
        } catch (IllegalArgumentException exception) {
            redirectAttributes.addFlashAttribute("erro", exception.getMessage());
            return "redirect:/paciente/minhas-consultas";
        }
    }

    @PostMapping("/consultas/{id}/cancelar")
    public String cancelarPorPaciente(@PathVariable Long id, Authentication authentication,
                                      RedirectAttributes redirectAttributes) {
        try {
            consultaService.cancelarPorPaciente(authentication.getName(), id);
            redirectAttributes.addFlashAttribute("sucesso", "Consulta cancelada com sucesso.");
        } catch (IllegalArgumentException exception) {
            redirectAttributes.addFlashAttribute("erro", exception.getMessage());
        }
        return "redirect:/paciente/minhas-consultas";
    }

    private AgendamentoDTO criarAgendamento(Long medicoId, LocalDate data, LocalTime horario) {
        AgendamentoDTO agendamento = new AgendamentoDTO();
        agendamento.setMedicoId(medicoId);
        agendamento.setData(data);
        agendamento.setHorario(horario);
        return agendamento;
    }
}
