package br.edu.mackenzie.agendamedico.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.mackenzie.agendamedico.model.Medico;

public interface MedicoRepository extends JpaRepository<Medico, Long> {

    Optional<Medico> findByEmail(String email);

    boolean existsByEmail(String email);
}
