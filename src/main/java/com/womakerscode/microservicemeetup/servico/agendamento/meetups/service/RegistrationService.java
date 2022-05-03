package com.womakerscode.microservicemeetup.servico.agendamento.meetups.service;

import com.womakerscode.microservicemeetup.servico.agendamento.meetups.controller.dto.RegistrationDTO;
import com.womakerscode.microservicemeetup.servico.agendamento.meetups.model.entity.Meetup;
import com.womakerscode.microservicemeetup.servico.agendamento.meetups.model.entity.Registration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface RegistrationService {

    Registration save(Registration registration);

    Integer save(RegistrationDTO registrationDTO, Optional<Meetup> meetupOptional);

    Optional<Registration> findByCode(String code);

    void delete(String code);

    Registration update(Registration registration);

    Registration update(RegistrationDTO registrationDTO);

    Page<Registration> findAll(Pageable pageRequest);

    boolean existMeetupOnRegistration(Integer id);

    Registration addMeetupInRegistration(RegistrationDTO registrationDTO, Optional<Meetup> meetupOptional);
}
