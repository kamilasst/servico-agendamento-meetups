package com.womakerscode.microservicemeetup.servico.agendamento.meetups.repository;

import com.womakerscode.microservicemeetup.servico.agendamento.meetups.model.entity.Meetup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MeetupRepository extends JpaRepository<Meetup, Integer> {

    Optional<Meetup> findByEvent(String event);
}
