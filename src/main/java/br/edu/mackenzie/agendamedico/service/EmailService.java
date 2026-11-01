package br.edu.mackenzie.agendamedico.service;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import br.edu.mackenzie.agendamedico.model.EmailEnviado;
import br.edu.mackenzie.agendamedico.repository.EmailEnviadoRepository;

@Service
public class EmailService {

    private static final Logger LOG = LoggerFactory.getLogger(EmailService.class);

    private final EmailEnviadoRepository emailRepository;
    private final ObjectProvider<JavaMailSender> mailSender;
    private final String remetente;

    public EmailService(EmailEnviadoRepository emailRepository, ObjectProvider<JavaMailSender> mailSender,
                        @Value("${agenda.email.remetente:nao-responda@agendamento-consultas.app}") String remetente) {
        this.emailRepository = emailRepository;
        this.mailSender = mailSender;
        this.remetente = remetente;
    }

    public void enviar(String destinatario, String assunto, String corpo, String tipo, Long consultaId) {
        EmailEnviado email = new EmailEnviado();
        email.setDestinatario(destinatario);
        email.setAssunto(assunto);
        email.setCorpo(corpo);
        email.setTipo(tipo);
        email.setConsultaId(consultaId);
        email.setCriadoEm(LocalDateTime.now());

        JavaMailSender sender = mailSender.getIfAvailable();
        if (sender != null) {
            try {
                SimpleMailMessage mensagem = new SimpleMailMessage();
                mensagem.setFrom(remetente);
                mensagem.setTo(destinatario);
                mensagem.setSubject(assunto);
                mensagem.setText(corpo);
                sender.send(mensagem);
                email.setEnviadoPorSmtp(true);
            } catch (RuntimeException exception) {
                LOG.warn("Falha ao enviar e-mail para {}: {}", destinatario, exception.getMessage());
            }
        }
        emailRepository.save(email);
    }
}
