package com.womakerscode.microservicemeetup.servico.agendamento.meetups.service;

import com.womakerscode.microservicemeetup.servico.agendamento.meetups.controller.dto.MeetupDTO;
import com.womakerscode.microservicemeetup.servico.agendamento.meetups.controller.dto.MeetupFilterDTO;
import com.womakerscode.microservicemeetup.servico.agendamento.meetups.controller.dto.MeetupRegistrationDTO;
import com.womakerscode.microservicemeetup.servico.agendamento.meetups.model.entity.Meetup;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface MeetupService  {

    Meetup save(Meetup meetup);

    Integer save(String event);

    Optional<Meetup> findById(Integer id);

    Meetup update(Meetup meetup);

    Meetup update(MeetupDTO meetupDto);

    void delete(Integer id);

    Optional<Meetup> findByEvent(MeetupFilterDTO filterDTO);

    Page<Meetup> findAll(Pageable pageRequest);

    List<MeetupRegistrationDTO> findAllRegistrationOnMeetup(MeetupFilterDTO meetupFilterDTO);

}
