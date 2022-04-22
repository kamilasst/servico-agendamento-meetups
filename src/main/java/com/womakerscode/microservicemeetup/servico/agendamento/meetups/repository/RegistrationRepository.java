package com.womakerscode.microservicemeetup.servico.agendamento.meetups.repository;

import com.womakerscode.microservicemeetup.servico.agendamento.meetups.model.entity.Registration;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RegistrationRepository extends JpaRepository<Registration, Integer> {

    boolean existsByCode(String code);

    Optional<Registration> findByCode(String code);
}
