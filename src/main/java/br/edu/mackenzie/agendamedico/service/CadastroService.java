package br.edu.mackenzie.agendamedico.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.edu.mackenzie.agendamedico.dto.CadastroDTO;
import br.edu.mackenzie.agendamedico.dto.TipoUsuario;
import br.edu.mackenzie.agendamedico.model.Medico;
import br.edu.mackenzie.agendamedico.model.Paciente;
import br.edu.mackenzie.agendamedico.repository.MedicoRepository;
import br.edu.mackenzie.agendamedico.repository.PacienteRepository;

@Service
public class CadastroService {

    private final PacienteRepository pacienteRepository;
    private final MedicoRepository medicoRepository;
    private final PasswordEncoder passwordEncoder;

    public CadastroService(PacienteRepository pacienteRepository, MedicoRepository medicoRepository,
                           PasswordEncoder passwordEncoder) {
        this.pacienteRepository = pacienteRepository;
        this.medicoRepository = medicoRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void cadastrar(CadastroDTO dados) {
        if (pacienteRepository.existsByEmail(dados.getEmail()) || medicoRepository.existsByEmail(dados.getEmail())) {
            throw new IllegalArgumentException("Já existe um usuário cadastrado com este e-mail.");
        }

        if (dados.getTipoUsuario() == TipoUsuario.MEDICO) {
            cadastrarMedico(dados);
            return;
        }

        Paciente paciente = new Paciente();
        paciente.setNome(dados.getNome());
        paciente.setEmail(dados.getEmail());
        paciente.setSenha(passwordEncoder.encode(dados.getSenha()));
        pacienteRepository.save(paciente);
    }

    private void cadastrarMedico(CadastroDTO dados) {
        if (dados.getEspecialidade() == null || dados.getEspecialidade().isBlank()) {
            throw new IllegalArgumentException("Informe a especialidade do médico.");
        }

        Medico medico = new Medico();
        medico.setNome(dados.getNome());
        medico.setEmail(dados.getEmail());
        medico.setSenha(passwordEncoder.encode(dados.getSenha()));
        medico.setEspecialidade(dados.getEspecialidade());
        medicoRepository.save(medico);
    }
}
