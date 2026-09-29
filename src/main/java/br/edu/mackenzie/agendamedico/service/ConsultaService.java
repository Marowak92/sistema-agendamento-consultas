package br.edu.mackenzie.agendamedico.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.edu.mackenzie.agendamedico.dto.AgendamentoDTO;
import br.edu.mackenzie.agendamedico.model.Agenda;
import br.edu.mackenzie.agendamedico.model.Consulta;
import br.edu.mackenzie.agendamedico.model.HorarioAtendimento;
import br.edu.mackenzie.agendamedico.model.Medico;
import br.edu.mackenzie.agendamedico.model.Paciente;
import br.edu.mackenzie.agendamedico.model.StatusConsulta;
import br.edu.mackenzie.agendamedico.repository.AgendaRepository;
import br.edu.mackenzie.agendamedico.repository.ConsultaRepository;
import br.edu.mackenzie.agendamedico.repository.HorarioAtendimentoRepository;
import br.edu.mackenzie.agendamedico.repository.MedicoRepository;
import br.edu.mackenzie.agendamedico.repository.PacienteRepository;

@Service
public class ConsultaService {

    private final MedicoRepository medicoRepository;
    private final PacienteRepository pacienteRepository;
    private final AgendaRepository agendaRepository;
    private final HorarioAtendimentoRepository horarioRepository;
    private final ConsultaRepository consultaRepository;

    public ConsultaService(MedicoRepository medicoRepository, PacienteRepository pacienteRepository,
                           AgendaRepository agendaRepository, HorarioAtendimentoRepository horarioRepository,
                           ConsultaRepository consultaRepository) {
        this.medicoRepository = medicoRepository;
        this.pacienteRepository = pacienteRepository;
        this.agendaRepository = agendaRepository;
        this.horarioRepository = horarioRepository;
        this.consultaRepository = consultaRepository;
    }

    public List<Medico> listarMedicos() {
        return medicoRepository.findAll();
    }

    public Medico obterMedico(Long medicoId) {
        return medicoRepository.findById(medicoId)
                .orElseThrow(() -> new IllegalArgumentException("Médico não encontrado."));
    }

    public List<LocalTime> horariosDisponiveis(Long medicoId, LocalDate data) {
        if (data.isBefore(LocalDate.now())) {
            return List.of();
        }

        Agenda agenda = agendaRepository.findByMedicoId(medicoId)
                .orElseThrow(() -> new IllegalArgumentException("Este médico ainda não possui agenda cadastrada."));
        List<HorarioAtendimento> periodos = horarioRepository
                .findByAgendaMedicoIdAndDataOrderByHoraInicioAsc(medicoId, data);
        Set<LocalTime> ocupados = consultaRepository.findByMedicoIdAndDataAndStatus(medicoId, data, StatusConsulta.AGENDADA)
                .stream().map(Consulta::getHorario).collect(Collectors.toSet());

        List<LocalTime> disponiveis = new ArrayList<>();
        for (HorarioAtendimento periodo : periodos) {
            LocalTime horario = periodo.getHoraInicio();
            while (!horario.plusMinutes(agenda.getDuracaoConsulta()).isAfter(periodo.getHoraFim())) {
                if (!ocupados.contains(horario)) {
                    disponiveis.add(horario);
                }
                horario = horario.plusMinutes(agenda.getDuracaoConsulta());
            }
        }
        return disponiveis;
    }

    public Consulta prepararResumo(AgendamentoDTO dados) {
        validarHorarioDisponivel(dados);
        return criarConsultaSemSalvar(null, dados);
    }

    @Transactional
    public void agendar(String emailPaciente, AgendamentoDTO dados) {
        validarHorarioDisponivel(dados);
        Paciente paciente = pacienteRepository.findByEmail(emailPaciente)
                .orElseThrow(() -> new IllegalArgumentException("Paciente não encontrado."));

        Consulta consulta = criarConsultaSemSalvar(paciente, dados);
        try {
            consultaRepository.saveAndFlush(consulta);
        } catch (DataIntegrityViolationException exception) {
            throw new IllegalArgumentException("Este horário não está mais disponível.");
        }
    }

    public List<Consulta> listarConsultasPaciente(String emailPaciente) {
        Paciente paciente = pacienteRepository.findByEmail(emailPaciente)
                .orElseThrow(() -> new IllegalArgumentException("Paciente não encontrado."));
        return consultaRepository.findByPacienteIdAndStatusOrderByDataAscHorarioAsc(paciente.getId(), StatusConsulta.AGENDADA);
    }

    public List<Consulta> listarConsultasMedico(String emailMedico) {
        Medico medico = medicoRepository.findByEmail(emailMedico)
                .orElseThrow(() -> new IllegalArgumentException("Médico não encontrado."));
        return consultaRepository.findByMedicoIdAndStatusOrderByDataAscHorarioAsc(medico.getId(), StatusConsulta.AGENDADA);
    }

    public Consulta obterConsultaPaciente(String emailPaciente, Long consultaId) {
        Consulta consulta = obterConsultaAgendada(consultaId);
        if (!consulta.getPaciente().getEmail().equals(emailPaciente)) {
            throw new IllegalArgumentException("Você só pode cancelar suas próprias consultas.");
        }
        return consulta;
    }

    public Consulta obterConsultaMedico(String emailMedico, Long consultaId) {
        Consulta consulta = obterConsultaAgendada(consultaId);
        if (!consulta.getMedico().getEmail().equals(emailMedico)) {
            throw new IllegalArgumentException("Você só pode cancelar consultas da sua agenda.");
        }
        return consulta;
    }

    @Transactional
    public void cancelarPorPaciente(String emailPaciente, Long consultaId) {
        Consulta consulta = obterConsultaPaciente(emailPaciente, consultaId);
        consulta.setStatus(StatusConsulta.CANCELADA);
    }

    @Transactional
    public void cancelarPorMedico(String emailMedico, Long consultaId) {
        Consulta consulta = obterConsultaMedico(emailMedico, consultaId);
        consulta.setStatus(StatusConsulta.CANCELADA);
    }

    private void validarHorarioDisponivel(AgendamentoDTO dados) {
        if (!horariosDisponiveis(dados.getMedicoId(), dados.getData()).contains(dados.getHorario())) {
            throw new IllegalArgumentException("Este horário não está mais disponível.");
        }
    }

    private Consulta obterConsultaAgendada(Long consultaId) {
        Consulta consulta = consultaRepository.findById(consultaId)
                .orElseThrow(() -> new IllegalArgumentException("Consulta não encontrada."));
        if (consulta.getStatus() != StatusConsulta.AGENDADA) {
            throw new IllegalArgumentException("Esta consulta não está mais agendada.");
        }
        return consulta;
    }

    private Consulta criarConsultaSemSalvar(Paciente paciente, AgendamentoDTO dados) {
        Medico medico = obterMedico(dados.getMedicoId());
        Agenda agenda = agendaRepository.findByMedicoId(medico.getId())
                .orElseThrow(() -> new IllegalArgumentException("Este médico ainda não possui agenda cadastrada."));

        Consulta consulta = new Consulta();
        consulta.setPaciente(paciente);
        consulta.setMedico(medico);
        consulta.setData(dados.getData());
        consulta.setHorario(dados.getHorario());
        consulta.setDuracao(agenda.getDuracaoConsulta());
        consulta.setValor(agenda.getValorConsulta());
        consulta.setStatus(StatusConsulta.AGENDADA);
        return consulta;
    }
}
