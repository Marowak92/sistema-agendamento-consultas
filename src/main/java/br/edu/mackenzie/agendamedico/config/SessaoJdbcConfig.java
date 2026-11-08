package br.edu.mackenzie.agendamedico.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.session.jdbc.PostgreSqlJdbcIndexedSessionRepositoryCustomizer;

@Configuration
@Profile("prod")
public class SessaoJdbcConfig {

    @Bean
    PostgreSqlJdbcIndexedSessionRepositoryCustomizer sessoesPostgres() {
        return new PostgreSqlJdbcIndexedSessionRepositoryCustomizer();
    }
}
