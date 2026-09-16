package br.edu.mackenzie.agendamedico.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import br.edu.mackenzie.agendamedico.dto.PainelDiarioDTO;
import br.edu.mackenzie.agendamedico.model.Agenda;
import br.edu.mackenzie.agendamedico.model.Consulta;
import br.edu.mackenzie.agendamedico.model.HorarioAtendimento;
import br.edu.mackenzie.agendamedico.model.Medico;
import br.edu.mackenzie.agendamedico.model.StatusConsulta;
import br.edu.mackenzie.agendamedico.repository.AgendaRepository;
import br.edu.mackenzie.agendamedico.repository.ConsultaRepository;
import br.edu.mackenzie.agendamedico.repository.HorarioAtendimentoRepository;
import br.edu.mackenzie.agendamedico.repository.MedicoRepository;

@Service
public class PainelMedicoService {

    private final MedicoRepository medicoRepository;
    private final AgendaRepository agendaRepository;
    private final HorarioAtendimentoRepository horarioRepository;
    private final ConsultaRepository consultaRepository;

    public PainelMedicoService(MedicoRepository medicoRepository, AgendaRepository agendaRepository,
                               HorarioAtendimentoRepository horarioRepository, ConsultaRepository consultaRepository) {
        this.medicoRepository = medicoRepository;
        this.agendaRepository = agendaRepository;
        this.horarioRepository = horarioRepository;
        this.consultaRepository = consultaRepository;
    }

    public PainelDiarioDTO montarPainel(String emailMedico, LocalDate data) {
        Medico medico = medicoRepository.findByEmail(emailMedico)
                .orElseThrow(() -> new IllegalArgumentException("Médico não encontrado."));
        List<Consulta> consultas = consultaRepository
                .findByMedicoIdAndDataAndStatus(medico.getId(), data, StatusConsulta.AGENDADA);

        PainelDiarioDTO painel = new PainelDiarioDTO();
        painel.setData(data);
        painel.setTotalConsultas(consultas.size());
        painel.setMinutosPrevistos(consultas.stream().mapToInt(Consulta::getDuracao).sum());
        painel.setValorPrevisto(consultas.stream().map(Consulta::getValor).reduce(BigDecimal.ZERO, BigDecimal::add));

        Agenda agenda = agendaRepository.findByMedicoId(medico.getId()).orElse(null);
        if (agenda == null) {
            return painel;
        }

        Map<LocalTime, Consulta> consultasPorHorario = consultas.stream()
                .collect(Collectors.toMap(Consulta::getHorario, Function.identity()));
        List<HorarioAtendimento> periodos = horarioRepository
                .findByAgendaMedicoIdAndDataOrderByHoraInicioAsc(medico.getId(), data);

        for (HorarioAtendimento periodo : periodos) {
            LocalTime horario = periodo.getHoraInicio();
            while (!horario.plusMinutes(agenda.getDuracaoConsulta()).isAfter(periodo.getHoraFim())) {
                painel.getAgenda().add(new PainelDiarioDTO.LinhaAgenda(horario, consultasPorHorario.get(horario)));
                horario = horario.plusMinutes(agenda.getDuracaoConsulta());
            }
        }
        return painel;
    }
}
