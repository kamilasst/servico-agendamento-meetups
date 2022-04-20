package com.womakerscode.microservicemeetup.servico.agendamento.meetups;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ServicoAgendamentoMeetupsApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServicoAgendamentoMeetupsApplication.class, args);
	}

	@Bean
	public ModelMapper modelMapper(){
		return new ModelMapper();
	}

}
