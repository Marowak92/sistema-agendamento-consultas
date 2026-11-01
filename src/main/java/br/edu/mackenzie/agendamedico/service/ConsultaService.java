package br.edu.mackenzie.agendamedico.service;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.edu.mackenzie.agendamedico.dto.AgendamentoDTO;
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

    static final long HORAS_MINIMAS_CANCELAMENTO = 24;
    private static final ZoneId FUSO = ZoneId.of("America/Sao_Paulo");
    private static final DateTimeFormatter HORA = DateTimeFormatter.ofPattern("HH:mm");
    private static final DateTimeFormatter ICS = DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss'Z'");

    private final MedicoRepository medicoRepository;
    private final PacienteRepository pacienteRepository;
    private final AgendaRepository agendaRepository;
    private final HorarioAtendimentoRepository horarioRepository;
    private final ConsultaRepository consultaRepository;
    private final NotificacaoService notificacaoService;

    public ConsultaService(MedicoRepository medicoRepository, PacienteRepository pacienteRepository,
                           AgendaRepository agendaRepository, HorarioAtendimentoRepository horarioRepository,
                           ConsultaRepository consultaRepository, NotificacaoService notificacaoService) {
        this.medicoRepository = medicoRepository;
        this.pacienteRepository = pacienteRepository;
        this.agendaRepository = agendaRepository;
        this.horarioRepository = horarioRepository;
        this.consultaRepository = consultaRepository;
        this.notificacaoService = notificacaoService;
    }

    public List<Medico> listarMedicos() {
        return medicoRepository.findAll();
    }

    /** Busca por parte do nome ou da especialidade, sem diferenciar maiusculas de minusculas. */
    public List<Medico> buscarMedicos(String termo) {
        if (termo == null || termo.isBlank()) {
            return listarMedicos();
        }
        String busca = termo.trim().toLowerCase(Locale.ROOT);
        return listarMedicos().stream()
                .filter(medico -> medico.getNome().toLowerCase(Locale.ROOT).contains(busca)
                        || medico.getEspecialidade().toLowerCase(Locale.ROOT).contains(busca))
                .toList();
    }

    public Medico obterMedico(Long medicoId) {
        return medicoRepository.findById(medicoId)
                .orElseThrow(() -> new IllegalArgumentException("Médico não encontrado."));
    }

    public List<LocalTime> horariosDisponiveis(Long medicoId, LocalDate data) {
        LocalDate hoje = LocalDate.now();
        if (data.isBefore(hoje)) {
            return List.of();
        }
        agendaRepository.findByMedicoId(medicoId)
                .orElseThrow(() -> new IllegalArgumentException("Este médico ainda não possui agenda cadastrada."));

        Set<LocalTime> ocupados = consultaRepository.findByMedicoIdAndDataAndStatus(medicoId, data, StatusConsulta.AGENDADA)
                .stream().map(Consulta::getHorario).collect(Collectors.toSet());
        LocalTime agora = LocalTime.now();

        List<LocalTime> disponiveis = new ArrayList<>();
        for (HorarioAtendimento periodo : horarioRepository.findByAgendaMedicoIdAndDataOrderByHoraInicioAsc(medicoId, data)) {
            for (LocalTime horario : periodo.horarios()) {
                boolean jaPassou = data.equals(hoje) && !horario.isAfter(agora);
                if (!ocupados.contains(horario) && !jaPassou) {
                    disponiveis.add(horario);
                }
            }
        }
        return disponiveis;
    }

    public Consulta prepararResumo(String emailPaciente, AgendamentoDTO dados) {
        validarHorarioDisponivel(dados);
        return criarConsultaSemSalvar(obterPaciente(emailPaciente), dados);
    }

    @Transactional
    public void agendar(String emailPaciente, AgendamentoDTO dados) {
        validarHorarioDisponivel(dados);
        Paciente paciente = obterPaciente(emailPaciente);
        validarConsultaNoMesmoDia(paciente, dados);
        Consulta consulta = criarConsultaSemSalvar(paciente, dados);
        try {
            consultaRepository.saveAndFlush(consulta);
        } catch (DataIntegrityViolationException exception) {
            throw new IllegalArgumentException("Este horário não está mais disponível.");
        }
        notificacaoService.lembrarSeDentroDoPrazo(consulta);
    }

    public List<Consulta> listarConsultasPaciente(String emailPaciente) {
        Paciente paciente = obterPaciente(emailPaciente);
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

    /** Paciente so cancela com pelo menos 24 horas de antecedencia (regra do UC03 definida na segunda iteracao). */
    public Consulta obterConsultaParaCancelamentoPaciente(String emailPaciente, Long consultaId) {
        Consulta consulta = obterConsultaPaciente(emailPaciente, consultaId);
        if (consulta.inicio().isBefore(LocalDateTime.now().plusHours(HORAS_MINIMAS_CANCELAMENTO))) {
            throw new IllegalArgumentException("O paciente só pode cancelar uma consulta com pelo menos 24 horas de "
                    + "antecedência. Para cancelar esta consulta, entre em contato com o médico.");
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
        Consulta consulta = obterConsultaParaCancelamentoPaciente(emailPaciente, consultaId);
        consulta.setStatus(StatusConsulta.CANCELADA);
        notificacaoService.avisarCancelamento(consulta, false);
    }

    @Transactional
    public void cancelarPorMedico(String emailMedico, Long consultaId) {
        Consulta consulta = obterConsultaMedico(emailMedico, consultaId);
        consulta.setStatus(StatusConsulta.CANCELADA);
        notificacaoService.avisarCancelamento(consulta, true);
    }

    /** Arquivo iCalendar (RFC 5545) da consulta, para importar na agenda pessoal (UC04 e ONF06). */
    public String gerarCalendario(String emailPaciente, Long consultaId) {
        Consulta consulta = consultaRepository.findById(consultaId)
                .orElseThrow(() -> new IllegalArgumentException("Consulta não encontrada."));
        if (!consulta.getPaciente().getEmail().equals(emailPaciente)) {
            throw new IllegalArgumentException("Você só pode exportar suas próprias consultas.");
        }
        if (consulta.getStatus() != StatusConsulta.AGENDADA) {
            throw new IllegalArgumentException("Esta consulta não está mais agendada.");
        }
        LocalDateTime inicio = consulta.inicio();
        String utcInicio = inicio.atZone(FUSO).withZoneSameInstant(ZoneOffset.UTC).format(ICS);
        String utcFim = inicio.plusMinutes(consulta.getDuracao()).atZone(FUSO).withZoneSameInstant(ZoneOffset.UTC).format(ICS);
        String agora = LocalDateTime.now(ZoneOffset.UTC).format(ICS);
        String medico = consulta.getMedico().getNome() + " (" + consulta.getMedico().getEspecialidade() + ")";
        return java.util.stream.Stream.of(
                "BEGIN:VCALENDAR",
                "VERSION:2.0",
                "PRODID:-//Grupo 24 Mackenzie//Sistema de Agendamento de Consultas//PT",
                "CALSCALE:GREGORIAN",
                "METHOD:PUBLISH",
                "BEGIN:VEVENT",
                "UID:consulta-" + consulta.getId() + "@agendamento-consultas",
                "DTSTAMP:" + agora,
                "DTSTART:" + utcInicio,
                "DTEND:" + utcFim,
                "SUMMARY:Consulta com " + escaparIcs(medico),
                "DESCRIPTION:" + escaparIcs("Consulta de " + consulta.getPaciente().getNome() + " com " + medico
                        + ", agendada pelo Sistema de Agendamento de Consultas."),
                "STATUS:CONFIRMED",
                "END:VEVENT",
                "END:VCALENDAR")
                .map(ConsultaService::dobrarLinha)
                .collect(Collectors.joining("\r\n", "", "\r\n"));
    }

    /** RFC 5545, secao 3.1: linhas com mais de 75 octetos continuam na linha seguinte, iniciada por um espaco. */
    private static String dobrarLinha(String linha) {
        StringBuilder dobrada = new StringBuilder();
        int octetos = 0;
        for (int i = 0; i < linha.length(); ) {
            int caractere = linha.codePointAt(i);
            int tamanho = new String(Character.toChars(caractere)).getBytes(StandardCharsets.UTF_8).length;
            if (octetos + tamanho > 75) {
                dobrada.append("\r\n ");
                octetos = 1;
            }
            dobrada.appendCodePoint(caractere);
            octetos += tamanho;
            i += Character.charCount(caractere);
        }
        return dobrada.toString();
    }

    private static String escaparIcs(String texto) {
        return texto.replace("\\", "\\\\").replace(";", "\\;").replace(",", "\\,").replace("\n", "\\n");
    }

    private Paciente obterPaciente(String emailPaciente) {
        return pacienteRepository.findByEmail(emailPaciente)
                .orElseThrow(() -> new IllegalArgumentException("Paciente não encontrado."));
    }

    /** UC02, fluxo 5b: o paciente nao marca duas consultas com o mesmo medico no mesmo dia. */
    private void validarConsultaNoMesmoDia(Paciente paciente, AgendamentoDTO dados) {
        consultaRepository.findByPacienteIdAndMedicoIdAndDataAndStatus(paciente.getId(), dados.getMedicoId(),
                        dados.getData(), StatusConsulta.AGENDADA).stream().findFirst()
                .ifPresent(existente -> {
                    throw new IllegalArgumentException("Você já tem uma consulta com este médico nesta data, às "
                            + existente.getHorario().format(HORA) + ". Para trocar o horário, cancele a consulta atual "
                            + "em \"Minhas consultas\".");
                });
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
        HorarioAtendimento periodo = horarioRepository
                .findByAgendaMedicoIdAndDataOrderByHoraInicioAsc(medico.getId(), dados.getData()).stream()
                .filter(item -> item.horarios().contains(dados.getHorario()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Este horário não está mais disponível."));

        Consulta consulta = new Consulta();
        consulta.setPaciente(paciente);
        consulta.setMedico(medico);
        consulta.setData(dados.getData());
        consulta.setHorario(dados.getHorario());
        consulta.setDuracao(periodo.duracaoEfetiva());
        consulta.setValor(periodo.valorEfetivo());
        consulta.setStatus(StatusConsulta.AGENDADA);
        return consulta;
    }
}
