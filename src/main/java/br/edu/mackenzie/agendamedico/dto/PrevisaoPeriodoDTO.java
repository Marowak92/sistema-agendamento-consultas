package br.edu.mackenzie.agendamedico.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

/** UC08: previsao de horas e valores entre duas datas, incluindo ambas. */
public class PrevisaoPeriodoDTO {

    private LocalDate dataInicial;
    private LocalDate dataFinal;
    private int totalConsultas;
    private int minutosPrevistos;
    private BigDecimal valorPrevisto = BigDecimal.ZERO;

    public LocalDate getDataInicial() { return dataInicial; }
    public void setDataInicial(LocalDate dataInicial) { this.dataInicial = dataInicial; }
    public LocalDate getDataFinal() { return dataFinal; }
    public void setDataFinal(LocalDate dataFinal) { this.dataFinal = dataFinal; }
    public int getTotalConsultas() { return totalConsultas; }
    public void setTotalConsultas(int totalConsultas) { this.totalConsultas = totalConsultas; }
    public int getMinutosPrevistos() { return minutosPrevistos; }
    public void setMinutosPrevistos(int minutosPrevistos) { this.minutosPrevistos = minutosPrevistos; }
    public BigDecimal getValorPrevisto() { return valorPrevisto; }
    public void setValorPrevisto(BigDecimal valorPrevisto) { this.valorPrevisto = valorPrevisto; }

    public boolean isValorInformado() {
        return totalConsultas == 0 || valorPrevisto.signum() > 0;
    }

    public String getHorasPrevistasFormatadas() {
        return minutosPrevistos / 60 + "h" + String.format("%02d", minutosPrevistos % 60);
    }
}
