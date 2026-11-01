package br.edu.mackenzie.agendamedico.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "horarios_atendimento")
public class HorarioAtendimento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "agenda_id", nullable = false)
    private Agenda agenda;

    @NotNull
    @Column(nullable = false)
    private LocalDate data;

    @NotNull
    @Column(nullable = false)
    private LocalTime horaInicio;

    @NotNull
    @Column(nullable = false)
    private LocalTime horaFim;

    /** Duracao e valor proprios do periodo. Periodos antigos, sem esses dados, usam os da agenda. */
    @Column(name = "duracao_consulta")
    private Integer duracaoConsulta;

    @Column(name = "valor_consulta", precision = 10, scale = 2)
    private BigDecimal valorConsulta;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Agenda getAgenda() {
        return agenda;
    }

    public void setAgenda(Agenda agenda) {
        this.agenda = agenda;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public LocalTime getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(LocalTime horaInicio) {
        this.horaInicio = horaInicio;
    }

    public LocalTime getHoraFim() {
        return horaFim;
    }

    public void setHoraFim(LocalTime horaFim) {
        this.horaFim = horaFim;
    }

    public Integer getDuracaoConsulta() {
        return duracaoConsulta;
    }

    public void setDuracaoConsulta(Integer duracaoConsulta) {
        this.duracaoConsulta = duracaoConsulta;
    }

    public BigDecimal getValorConsulta() {
        return valorConsulta;
    }

    public void setValorConsulta(BigDecimal valorConsulta) {
        this.valorConsulta = valorConsulta;
    }

    public int duracaoEfetiva() {
        return duracaoConsulta != null ? duracaoConsulta : agenda.getDuracaoConsulta();
    }

    public BigDecimal valorEfetivo() {
        return valorConsulta != null ? valorConsulta : agenda.getValorConsulta();
    }

    /** Horarios de inicio das consultas que cabem inteiras no periodo. */
    public java.util.List<LocalTime> horarios() {
        java.util.List<LocalTime> lista = new java.util.ArrayList<>();
        LocalTime horario = horaInicio;
        while (!horario.plusMinutes(duracaoEfetiva()).isAfter(horaFim) && !horario.plusMinutes(duracaoEfetiva()).isBefore(horario)) {
            lista.add(horario);
            horario = horario.plusMinutes(duracaoEfetiva());
        }
        return lista;
    }
}
