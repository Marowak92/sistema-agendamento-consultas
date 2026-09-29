package br.edu.mackenzie.agendamedico.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import br.edu.mackenzie.agendamedico.dto.HorarioAtendimentoDTO;
import br.edu.mackenzie.agendamedico.model.Agenda;
import br.edu.mackenzie.agendamedico.model.HorarioAtendimento;
import br.edu.mackenzie.agendamedico.model.Medico;
import br.edu.mackenzie.agendamedico.repository.AgendaRepository;
import br.edu.mackenzie.agendamedico.repository.HorarioAtendimentoRepository;
import br.edu.mackenzie.agendamedico.repository.MedicoRepository;

@Service
public class AgendaService {

    private final AgendaRepository agendaRepository;
    private final HorarioAtendimentoRepository horarioRepository;
    private final MedicoRepository medicoRepository;

    public AgendaService(AgendaRepository agendaRepository, HorarioAtendimentoRepository horarioRepository,
                         MedicoRepository medicoRepository) {
        this.agendaRepository = agendaRepository;
        this.horarioRepository = horarioRepository;
        this.medicoRepository = medicoRepository;
    }

    public HorarioAtendimentoDTO novoHorario(String emailMedico) {
        Agenda agenda = obterAgenda(emailMedico);
        HorarioAtendimentoDTO dados = new HorarioAtendimentoDTO();
        dados.setData(LocalDate.now());
        dados.setDuracaoConsulta(agenda.getDuracaoConsulta());
        dados.setValorConsulta(agenda.getValorConsulta());
        return dados;
    }

    public List<HorarioAtendimento> listarHorarios(String emailMedico, LocalDate data) {
        Medico medico = obterMedico(emailMedico);
        return horarioRepository.findByAgendaMedicoIdAndDataOrderByHoraInicioAsc(medico.getId(), data);
    }

    public void adicionarHorario(String emailMedico, HorarioAtendimentoDTO dados) {
        if (!dados.getHoraFim().isAfter(dados.getHoraInicio())) {
            throw new IllegalArgumentException("A hora final deve ser maior que a hora inicial.");
        }

        Agenda agenda = obterAgenda(emailMedico);
        List<HorarioAtendimento> existentes = horarioRepository
                .findByAgendaMedicoIdAndDataOrderByHoraInicioAsc(agenda.getMedico().getId(), dados.getData());

        boolean possuiConflito = existentes.stream().anyMatch(horario ->
                dados.getHoraInicio().isBefore(horario.getHoraFim())
                        && dados.getHoraFim().isAfter(horario.getHoraInicio()));
        if (possuiConflito) {
            throw new IllegalArgumentException("Esse período entra em conflito com outro horário de atendimento.");
        }

        agenda.setDuracaoConsulta(dados.getDuracaoConsulta());
        agenda.setValorConsulta(dados.getValorConsulta());
        agendaRepository.save(agenda);

        HorarioAtendimento horario = new HorarioAtendimento();
        horario.setAgenda(agenda);
        horario.setData(dados.getData());
        horario.setHoraInicio(dados.getHoraInicio());
        horario.setHoraFim(dados.getHoraFim());
        horarioRepository.save(horario);
    }

    public void excluirHorario(String emailMedico, Long horarioId) {
        HorarioAtendimento horario = horarioRepository.findById(horarioId)
                .orElseThrow(() -> new IllegalArgumentException("Horário de atendimento não encontrado."));

        if (!horario.getAgenda().getMedico().getEmail().equals(emailMedico)) {
            throw new IllegalArgumentException("Você não pode alterar a agenda de outro médico.");
        }
        horarioRepository.delete(horario);
    }

    private Agenda obterAgenda(String emailMedico) {
        Medico medico = obterMedico(emailMedico);
        return agendaRepository.findByMedicoId(medico.getId()).orElseGet(() -> {
            Agenda agenda = new Agenda();
            agenda.setMedico(medico);
            agenda.setDuracaoConsulta(30);
            agenda.setValorConsulta(BigDecimal.ZERO);
            return agendaRepository.save(agenda);
        });
    }

    private Medico obterMedico(String emailMedico) {
        return medicoRepository.findByEmail(emailMedico)
                .orElseThrow(() -> new IllegalArgumentException("Médico não encontrado."));
    }
}
