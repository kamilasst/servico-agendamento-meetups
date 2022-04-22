package com.womakerscode.microservicemeetup.servico.agendamento.meetups.repository;

import com.womakerscode.microservicemeetup.servico.agendamento.meetups.model.entity.Meetup;
import com.womakerscode.microservicemeetup.servico.agendamento.meetups.model.entity.Registration;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MeetupRepository extends JpaRepository<Meetup, Integer> {

 //   @Query( value = " select m from Meetup as m where m.event =:event ")
    Page<Meetup> findByEvent(
            @Param("event") String event,
            Pageable pageable
    );

    //Page<Meetup> findByRegistration(Registration registration, Pageable pageable );

}
