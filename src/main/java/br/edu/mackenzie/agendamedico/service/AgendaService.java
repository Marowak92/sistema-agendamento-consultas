package br.edu.mackenzie.agendamedico.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.edu.mackenzie.agendamedico.dto.HorarioAtendimentoDTO;
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
public class AgendaService {

    private static final DateTimeFormatter DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter HORA = DateTimeFormatter.ofPattern("HH:mm");

    private final AgendaRepository agendaRepository;
    private final HorarioAtendimentoRepository horarioRepository;
    private final MedicoRepository medicoRepository;
    private final ConsultaRepository consultaRepository;

    public AgendaService(AgendaRepository agendaRepository, HorarioAtendimentoRepository horarioRepository,
                         MedicoRepository medicoRepository, ConsultaRepository consultaRepository) {
        this.agendaRepository = agendaRepository;
        this.horarioRepository = horarioRepository;
        this.medicoRepository = medicoRepository;
        this.consultaRepository = consultaRepository;
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

    @Transactional
    public void adicionarHorario(String emailMedico, HorarioAtendimentoDTO dados) {
        validarHoras(dados);
        Agenda agenda = obterAgenda(emailMedico);
        validarConflito(agenda, dados, null);

        // a duracao e o valor informados passam a ser so o padrao sugerido no formulario
        agenda.setDuracaoConsulta(dados.getDuracaoConsulta());
        agenda.setValorConsulta(dados.getValorConsulta());
        agendaRepository.save(agenda);

        HorarioAtendimento horario = new HorarioAtendimento();
        horario.setAgenda(agenda);
        preencher(horario, dados);
        horarioRepository.save(horario);
    }

    public HorarioAtendimento obterHorarioDoMedico(String emailMedico, Long horarioId) {
        HorarioAtendimento horario = horarioRepository.findById(horarioId)
                .orElseThrow(() -> new IllegalArgumentException("Horário de atendimento não encontrado."));
        if (!horario.getAgenda().getMedico().getEmail().equals(emailMedico)) {
            throw new IllegalArgumentException("Você não pode alterar a agenda de outro médico.");
        }
        return horario;
    }

    public HorarioAtendimentoDTO dadosDoHorario(HorarioAtendimento horario) {
        HorarioAtendimentoDTO dados = new HorarioAtendimentoDTO();
        dados.setData(horario.getData());
        dados.setHoraInicio(horario.getHoraInicio());
        dados.setHoraFim(horario.getHoraFim());
        dados.setDuracaoConsulta(horario.duracaoEfetiva());
        dados.setValorConsulta(horario.valorEfetivo());
        return dados;
    }

    /** UC06: alteracao de periodo. Consultas que deixariam de caber no novo periodo impedem a alteracao. */
    @Transactional
    public void alterarHorario(String emailMedico, Long horarioId, HorarioAtendimentoDTO dados) {
        HorarioAtendimento horario = obterHorarioDoMedico(emailMedico, horarioId);
        validarHoras(dados);
        validarConflito(horario.getAgenda(), dados, horario.getId());

        List<Consulta> afetadas = consultasDoPeriodo(horario).stream()
                .filter(consulta -> !cabeNoNovoPeriodo(consulta, dados))
                .toList();
        if (!afetadas.isEmpty()) {
            throw new IllegalArgumentException("A alteração deixaria sem horário as consultas de " + listar(afetadas)
                    + ". Cancele essas consultas antes de alterar o período.");
        }
        preencher(horario, dados);
        horarioRepository.save(horario);
    }

    /** UC06, fluxo 3b: periodo com consultas agendadas so e removido depois que elas forem canceladas. */
    @Transactional
    public void excluirHorario(String emailMedico, Long horarioId) {
        HorarioAtendimento horario = obterHorarioDoMedico(emailMedico, horarioId);
        List<Consulta> afetadas = consultasDoPeriodo(horario);
        if (!afetadas.isEmpty()) {
            throw new IllegalArgumentException("Este período tem consultas agendadas: " + listar(afetadas)
                    + ". Cancele essas consultas antes de remover o período.");
        }
        horario.getAgenda().getHorariosAtendimento().remove(horario);
        horarioRepository.delete(horario);
    }

    private List<Consulta> consultasDoPeriodo(HorarioAtendimento horario) {
        return consultaRepository.findByMedicoIdAndDataAndStatus(horario.getAgenda().getMedico().getId(),
                        horario.getData(), StatusConsulta.AGENDADA).stream()
                .filter(consulta -> !consulta.getHorario().isBefore(horario.getHoraInicio())
                        && consulta.getHorario().isBefore(horario.getHoraFim()))
                .sorted((a, b) -> a.getHorario().compareTo(b.getHorario()))
                .toList();
    }

    private boolean cabeNoNovoPeriodo(Consulta consulta, HorarioAtendimentoDTO dados) {
        if (!consulta.getData().equals(dados.getData())) {
            return false;
        }
        LocalTime horario = consulta.getHorario();
        long desdeInicio = ChronoUnit.MINUTES.between(dados.getHoraInicio(), horario);
        return desdeInicio >= 0
                && desdeInicio % dados.getDuracaoConsulta() == 0
                && !horario.plusMinutes(dados.getDuracaoConsulta()).isAfter(dados.getHoraFim());
    }

    private static String listar(List<Consulta> consultas) {
        return consultas.stream()
                .map(c -> c.getPaciente().getNome() + " em " + c.getData().format(DATA) + " às " + c.getHorario().format(HORA))
                .collect(Collectors.joining(", "));
    }

    private static void validarHoras(HorarioAtendimentoDTO dados) {
        if (!dados.getHoraFim().isAfter(dados.getHoraInicio())) {
            throw new IllegalArgumentException("A hora final deve ser maior que a hora inicial.");
        }
    }

    private void validarConflito(Agenda agenda, HorarioAtendimentoDTO dados, Long ignorarId) {
        boolean possuiConflito = horarioRepository
                .findByAgendaMedicoIdAndDataOrderByHoraInicioAsc(agenda.getMedico().getId(), dados.getData()).stream()
                .filter(horario -> !horario.getId().equals(ignorarId))
                .anyMatch(horario -> dados.getHoraInicio().isBefore(horario.getHoraFim())
                        && dados.getHoraFim().isAfter(horario.getHoraInicio()));
        if (possuiConflito) {
            throw new IllegalArgumentException("Esse período entra em conflito com outro horário de atendimento.");
        }
    }

    private static void preencher(HorarioAtendimento horario, HorarioAtendimentoDTO dados) {
        horario.setData(dados.getData());
        horario.setHoraInicio(dados.getHoraInicio());
        horario.setHoraFim(dados.getHoraFim());
        horario.setDuracaoConsulta(dados.getDuracaoConsulta());
        horario.setValorConsulta(dados.getValorConsulta());
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
