package br.edu.mackenzie.agendamedico.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.edu.mackenzie.agendamedico.model.Consulta;
import br.edu.mackenzie.agendamedico.model.StatusConsulta;
import br.edu.mackenzie.agendamedico.repository.ConsultaRepository;

/**
 * Lembrete 48 horas antes da consulta e aviso de cancelamento, pelas regras definidas para a segunda
 * iteracao: consulta marcada com menos de 48 horas recebe o lembrete logo apos o agendamento, e consulta
 * cancelada nao recebe lembrete.
 */
@Service
public class NotificacaoService {

    static final long HORAS_LEMBRETE = 48;
    private static final DateTimeFormatter DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter HORA = DateTimeFormatter.ofPattern("HH:mm");

    private final ConsultaRepository consultaRepository;
    private final EmailService emailService;

    public NotificacaoService(ConsultaRepository consultaRepository, EmailService emailService) {
        this.consultaRepository = consultaRepository;
        this.emailService = emailService;
    }

    @Transactional
    public void lembrarSeDentroDoPrazo(Consulta consulta) {
        LocalDateTime agora = LocalDateTime.now();
        if (consulta.getStatus() == StatusConsulta.AGENDADA && !consulta.isLembreteEnviado()
                && consulta.inicio().isAfter(agora) && !consulta.inicio().isAfter(agora.plusHours(HORAS_LEMBRETE))) {
            emailService.enviar(consulta.getPaciente().getEmail(),
                    "Lembrete: consulta em " + consulta.getData().format(DATA) + " às " + consulta.getHorario().format(HORA),
                    "Olá, " + consulta.getPaciente().getNome() + ".\n\nLembramos que você tem consulta com "
                            + consulta.getMedico().getNome() + " (" + consulta.getMedico().getEspecialidade() + ") em "
                            + consulta.getData().format(DATA) + ", às " + consulta.getHorario().format(HORA)
                            + ".\n\nSe não puder comparecer, cancele pelo sistema com pelo menos 24 horas de antecedência.",
                    "LEMBRETE", consulta.getId());
            consulta.setLembreteEnviado(true);
            consultaRepository.save(consulta);
        }
    }

    /** Verifica periodicamente as consultas que entraram na janela de 48 horas. */
    @Scheduled(fixedDelayString = "${agenda.lembrete.intervalo-ms:60000}", initialDelay = 10000)
    @Transactional
    public void enviarLembretesPendentes() {
        LocalDateTime agora = LocalDateTime.now();
        List<Consulta> proximas = consultaRepository.findByStatusAndDataBetween(StatusConsulta.AGENDADA,
                agora.toLocalDate(), agora.plusHours(HORAS_LEMBRETE).toLocalDate());
        proximas.forEach(this::lembrarSeDentroDoPrazo);
    }

    public void avisarCancelamento(Consulta consulta, boolean canceladaPeloMedico) {
        String quando = consulta.getData().format(DATA) + ", às " + consulta.getHorario().format(HORA);
        if (canceladaPeloMedico) {
            emailService.enviar(consulta.getPaciente().getEmail(), "Consulta cancelada pelo médico",
                    "Olá, " + consulta.getPaciente().getNome() + ".\n\n" + consulta.getMedico().getNome()
                            + " cancelou a sua consulta de " + quando
                            + ".\n\nVocê pode escolher outro horário no sistema de agendamento.",
                    "CANCELAMENTO", consulta.getId());
        } else {
            emailService.enviar(consulta.getMedico().getEmail(), "Consulta cancelada pelo paciente",
                    "Olá, " + consulta.getMedico().getNome() + ".\n\n" + consulta.getPaciente().getNome()
                            + " cancelou a consulta de " + quando + ". O horário voltou a ficar livre na sua agenda.",
                    "CANCELAMENTO", consulta.getId());
        }
    }
}
