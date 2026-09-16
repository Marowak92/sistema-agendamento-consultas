package br.edu.mackenzie.agendamedico.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.mackenzie.agendamedico.model.HorarioAtendimento;

public interface HorarioAtendimentoRepository extends JpaRepository<HorarioAtendimento, Long> {

    List<HorarioAtendimento> findByAgendaMedicoIdAndDataOrderByHoraInicioAsc(Long medicoId, LocalDate data);
}
