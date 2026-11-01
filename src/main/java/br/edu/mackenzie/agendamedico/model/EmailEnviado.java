package br.edu.mackenzie.agendamedico.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Registro de cada e-mail gerado pelo sistema (caixa de saida). Em producao o e-mail tambem e
 * enviado pelo servidor SMTP configurado; no perfil de desenvolvimento fica so o registro.
 */
@Entity
@Table(name = "emails_enviados")
public class EmailEnviado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String destinatario;

    @Column(nullable = false)
    private String assunto;

    @Column(nullable = false, length = 2000)
    private String corpo;

    @Column(nullable = false, length = 20)
    private String tipo;

    private Long consultaId;

    @Column(nullable = false)
    private LocalDateTime criadoEm;

    @Column(nullable = false)
    private boolean enviadoPorSmtp;

    public Long getId() {
        return id;
    }

    public String getDestinatario() {
        return destinatario;
    }

    public void setDestinatario(String destinatario) {
        this.destinatario = destinatario;
    }

    public String getAssunto() {
        return assunto;
    }

    public void setAssunto(String assunto) {
        this.assunto = assunto;
    }

    public String getCorpo() {
        return corpo;
    }

    public void setCorpo(String corpo) {
        this.corpo = corpo;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Long getConsultaId() {
        return consultaId;
    }

    public void setConsultaId(Long consultaId) {
        this.consultaId = consultaId;
    }

    public LocalDateTime getCriadoEm() {
        return criadoEm;
    }

    public void setCriadoEm(LocalDateTime criadoEm) {
        this.criadoEm = criadoEm;
    }

    public boolean isEnviadoPorSmtp() {
        return enviadoPorSmtp;
    }

    public void setEnviadoPorSmtp(boolean enviadoPorSmtp) {
        this.enviadoPorSmtp = enviadoPorSmtp;
    }
}
