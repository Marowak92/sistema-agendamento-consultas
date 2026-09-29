package br.edu.mackenzie.agendamedico.service;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.edu.mackenzie.agendamedico.repository.MedicoRepository;
import br.edu.mackenzie.agendamedico.repository.PacienteRepository;

@Service
public class AutenticacaoService implements UserDetailsService {

    private final PacienteRepository pacienteRepository;
    private final MedicoRepository medicoRepository;

    public AutenticacaoService(PacienteRepository pacienteRepository, MedicoRepository medicoRepository) {
        this.pacienteRepository = pacienteRepository;
        this.medicoRepository = medicoRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return pacienteRepository.findByEmail(email)
                .<UserDetails>map(paciente -> User.withUsername(paciente.getEmail())
                        .password(paciente.getSenha())
                        .roles("PACIENTE")
                        .build())
                .or(() -> medicoRepository.findByEmail(email)
                        .map(medico -> User.withUsername(medico.getEmail())
                                .password(medico.getSenha())
                                .roles("MEDICO")
                                .build()))
                .orElseThrow(() -> new UsernameNotFoundException("E-mail ou senha inválidos."));
    }
}
