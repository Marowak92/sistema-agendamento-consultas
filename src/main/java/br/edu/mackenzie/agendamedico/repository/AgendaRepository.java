package br.edu.mackenzie.agendamedico.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.mackenzie.agendamedico.model.Agenda;

public interface AgendaRepository extends JpaRepository<Agenda, Long> {

    Optional<Agenda> findByMedicoId(Long medicoId);
}
