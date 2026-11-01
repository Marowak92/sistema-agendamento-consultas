package br.edu.mackenzie.agendamedico.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.mackenzie.agendamedico.model.EmailEnviado;

public interface EmailEnviadoRepository extends JpaRepository<EmailEnviado, Long> {

    List<EmailEnviado> findAllByOrderByIdDesc();
}
