package br.edu.mackenzie.agendamedico.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import br.edu.mackenzie.agendamedico.model.Consulta;

public class PainelDiarioDTO {

    private LocalDate data;
    private int totalConsultas;
    private int minutosPrevistos;
    private BigDecimal valorPrevisto = BigDecimal.ZERO;
    private List<LinhaAgenda> agenda = new ArrayList<>();

    public LocalDate getData() { return data; }
    public void setData(LocalDate data) { this.data = data; }
    public int getTotalConsultas() { return totalConsultas; }
    public void setTotalConsultas(int totalConsultas) { this.totalConsultas = totalConsultas; }
    public int getMinutosPrevistos() { return minutosPrevistos; }
    public void setMinutosPrevistos(int minutosPrevistos) { this.minutosPrevistos = minutosPrevistos; }
    public BigDecimal getValorPrevisto() { return valorPrevisto; }
    public void setValorPrevisto(BigDecimal valorPrevisto) { this.valorPrevisto = valorPrevisto; }
    public List<LinhaAgenda> getAgenda() { return agenda; }
    public void setAgenda(List<LinhaAgenda> agenda) { this.agenda = agenda; }

    public String getHorasPrevistasFormatadas() {
        return minutosPrevistos / 60 + "h" + String.format("%02d", minutosPrevistos % 60);
    }

    public static class LinhaAgenda {
        private LocalTime horario;
        private Consulta consulta;

        public LinhaAgenda(LocalTime horario, Consulta consulta) {
            this.horario = horario;
            this.consulta = consulta;
        }

        public LocalTime getHorario() { return horario; }
        public Consulta getConsulta() { return consulta; }
        public boolean isLivre() { return consulta == null; }
    }
}
