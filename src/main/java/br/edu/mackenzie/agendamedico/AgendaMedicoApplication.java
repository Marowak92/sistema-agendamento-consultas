package br.edu.mackenzie.agendamedico;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class AgendaMedicoApplication {

    public static void main(String[] args) {
        SpringApplication.run(AgendaMedicoApplication.class, args);
    }
}
