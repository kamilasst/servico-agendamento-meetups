package com.womakerscode.microservicemeetup.servico.agendamento.meetups.service.impl;

import com.womakerscode.microservicemeetup.servico.agendamento.meetups.controller.dto.MeetupFilterDTO;
import com.womakerscode.microservicemeetup.servico.agendamento.meetups.model.entity.Meetup;
import com.womakerscode.microservicemeetup.servico.agendamento.meetups.model.entity.Registration;
import com.womakerscode.microservicemeetup.servico.agendamento.meetups.repository.MeetupRepository;
import com.womakerscode.microservicemeetup.servico.agendamento.meetups.service.MeetupService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MeetupServiceImpl implements MeetupService {

    private MeetupRepository meetupRepository;

    public MeetupServiceImpl(MeetupRepository repository) {
        this.meetupRepository = repository;
    }

    @Override
    public Meetup save(Meetup meetup) {
        return meetupRepository.save(meetup);
    }

    @Override
    public Optional<Meetup> getById(Integer id) {
        return meetupRepository.findById(id);
    }

    @Override
    public Meetup update(Meetup loan) {
        return meetupRepository.save(loan);
    }

    @Override
    public Page<Meetup> find(MeetupFilterDTO filterDTO, Pageable pageable) {
        return meetupRepository.findByEvent(filterDTO.getEvent(), pageable);
    }

//    @Override
//    public Page<Meetup> getRegistrationsByMeetup(Registration registration, Pageable pageable) {
//        return meetupRepository.findByRegistration(registration, pageable);
//    }
}
