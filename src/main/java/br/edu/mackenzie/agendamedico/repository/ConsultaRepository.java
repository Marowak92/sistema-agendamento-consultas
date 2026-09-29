package br.edu.mackenzie.agendamedico.repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.mackenzie.agendamedico.model.Consulta;
import br.edu.mackenzie.agendamedico.model.StatusConsulta;

public interface ConsultaRepository extends JpaRepository<Consulta, Long> {

    boolean existsByMedicoIdAndDataAndHorarioAndStatus(Long medicoId, LocalDate data, LocalTime horario,
                                                       StatusConsulta status);

    List<Consulta> findByMedicoIdAndDataAndStatus(Long medicoId, LocalDate data, StatusConsulta status);

    List<Consulta> findByPacienteIdAndStatusOrderByDataAscHorarioAsc(Long pacienteId, StatusConsulta status);

    List<Consulta> findByMedicoIdAndStatusOrderByDataAscHorarioAsc(Long medicoId, StatusConsulta status);
}
