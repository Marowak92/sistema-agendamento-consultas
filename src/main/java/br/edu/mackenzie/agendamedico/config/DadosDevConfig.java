package br.edu.mackenzie.agendamedico.config;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

import br.edu.mackenzie.agendamedico.model.Agenda;
import br.edu.mackenzie.agendamedico.model.HorarioAtendimento;
import br.edu.mackenzie.agendamedico.model.Medico;
import br.edu.mackenzie.agendamedico.model.Paciente;
import br.edu.mackenzie.agendamedico.repository.AgendaRepository;
import br.edu.mackenzie.agendamedico.repository.MedicoRepository;
import br.edu.mackenzie.agendamedico.repository.PacienteRepository;

@Configuration
@Profile("dev")
public class DadosDevConfig {

    @Bean
    CommandLineRunner carregarDadosDev(MedicoRepository medicoRepository,
                                      PacienteRepository pacienteRepository,
                                      AgendaRepository agendaRepository,
                                      PasswordEncoder passwordEncoder) {
        return args -> {
            if (medicoRepository.count() > 0) {
                return;
            }

            Medico ana = salvarMedico(medicoRepository, "Ana Lima", "Cardiologista", "ana.lima@teste.com", passwordEncoder);
            Medico bruno = salvarMedico(medicoRepository, "Bruno Melo", "Dermatologista", "bruno.melo@teste.com", passwordEncoder);
            Medico carlos = salvarMedico(medicoRepository, "Carlos Reis", "Pediatra", "carlos.reis@teste.com", passwordEncoder);

            salvarAgenda(agendaRepository, ana);
            salvarAgenda(agendaRepository, bruno);
            salvarAgenda(agendaRepository, carlos);

            Paciente paciente = new Paciente();
            paciente.setNome("Paciente Teste");
            paciente.setEmail("paciente@teste.com");
            paciente.setSenha(passwordEncoder.encode("1234"));
            pacienteRepository.save(paciente);
        };
    }

    private Medico salvarMedico(MedicoRepository repository, String nome, String especialidade,
                                String email, PasswordEncoder passwordEncoder) {
        Medico medico = new Medico();
        medico.setNome(nome);
        medico.setEspecialidade(especialidade);
        medico.setEmail(email);
        medico.setSenha(passwordEncoder.encode("1234"));
        return repository.save(medico);
    }

    private void salvarAgenda(AgendaRepository repository, Medico medico) {
        Agenda agenda = new Agenda();
        agenda.setMedico(medico);
        agenda.setDuracaoConsulta(30);
        agenda.setValorConsulta(new BigDecimal("150.00"));

        HorarioAtendimento horario = new HorarioAtendimento();
        horario.setAgenda(agenda);
        horario.setData(LocalDate.now().plusDays(1));
        horario.setHoraInicio(LocalTime.of(8, 0));
        horario.setHoraFim(LocalTime.of(12, 0));
        agenda.getHorariosAtendimento().add(horario);

        repository.save(agenda);
    }
}
