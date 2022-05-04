package com.womakerscode.microservicemeetup.servico.agendamento.meetups.service;

import com.womakerscode.microservicemeetup.servico.agendamento.meetups.controller.dto.RegistrationDTO;
import com.womakerscode.microservicemeetup.servico.agendamento.meetups.model.entity.Meetup;
import com.womakerscode.microservicemeetup.servico.agendamento.meetups.model.entity.Registration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface RegistrationService {

    Integer save(RegistrationDTO registrationDTO, Optional<Meetup> meetupOptional);

    void delete(String code);

    Registration update(RegistrationDTO registrationDTO);

    Optional<Registration> findByCode(String code);

    Page<Registration> findAll(Pageable pageRequest);

    boolean existMeetupOnRegistration(Integer id);

    Registration addMeetupInRegistration(RegistrationDTO registrationDTO, Optional<Meetup> meetupOptional);
}
