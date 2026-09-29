package br.edu.mackenzie.agendamedico.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class HorarioAtendimentoDTO {

    @NotNull(message = "Informe a data de atendimento.")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate data;

    @NotNull(message = "Informe a hora inicial.")
    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    private LocalTime horaInicio;

    @NotNull(message = "Informe a hora final.")
    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    private LocalTime horaFim;

    @NotNull(message = "Informe a duração da consulta.")
    @Positive(message = "A duração deve ser maior que zero.")
    private Integer duracaoConsulta;

    @NotNull(message = "Informe o valor da consulta.")
    @DecimalMin(value = "0.0", inclusive = true, message = "O valor não pode ser negativo.")
    private BigDecimal valorConsulta;

    public LocalDate getData() { return data; }
    public void setData(LocalDate data) { this.data = data; }
    public LocalTime getHoraInicio() { return horaInicio; }
    public void setHoraInicio(LocalTime horaInicio) { this.horaInicio = horaInicio; }
    public LocalTime getHoraFim() { return horaFim; }
    public void setHoraFim(LocalTime horaFim) { this.horaFim = horaFim; }
    public Integer getDuracaoConsulta() { return duracaoConsulta; }
    public void setDuracaoConsulta(Integer duracaoConsulta) { this.duracaoConsulta = duracaoConsulta; }
    public BigDecimal getValorConsulta() { return valorConsulta; }
    public void setValorConsulta(BigDecimal valorConsulta) { this.valorConsulta = valorConsulta; }
}
