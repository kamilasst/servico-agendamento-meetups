package com.womakerscode.microservicemeetup.servico.agendamento.meetups.repository;

import com.womakerscode.microservicemeetup.servico.agendamento.meetups.controller.dto.MeetupRegistrationDTO;
import com.womakerscode.microservicemeetup.servico.agendamento.meetups.model.entity.Meetup;
import com.womakerscode.microservicemeetup.servico.agendamento.meetups.model.entity.Registration;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MeetupRepository extends JpaRepository<Meetup, Integer> {

    @Query(value = " select m from Meetup as m where m.event =:event ")
    Optional<Meetup> findByEvent(@Param("event") String event);

    @Query(value = "SELECT COUNT(ID) FROM REGISTRATION AS R WHERE R.ID_MEETUP = :ID", nativeQuery = true)
    Integer getCountRegistrationAssociatedMeetup(@Param("ID")Integer id);

    @Query(value = "SELECT M.ID, M.EVENT.M.DATE, M.REGISTERED, R.CODE, R.CPF, R.DATEOFREGISTRATION, R.NAME FROM MEETUP AS M INNER JOIN REGISTRATION AS R ON M.ID = R.MEETUP.ID WHERE M.EVENT = :event", nativeQuery = true)
    List<MeetupRegistrationDTO> findAllRegistrationOnMeetup(@Param ("event") String event);






//    @Query(value = "SELECT M.*, R.CODE, R.PERSON_CPF, R.DATE_OF_REGISTRATION, R.PERSON_NAME FROM MEETUP AS M INNER JOIN REGISTRATION AS R ON M.ID = R.ID_MEETUP WHERE M.EVENT = :event", nativeQuery = true)
//    Page<MeetupRegistrationDTO> findAllRegistrationOnMeetup(@Param ("event") String event, Pageable pageable);


    //Page<Meetup> findByRegistration(Registration registration, Pageable pageable );

}
