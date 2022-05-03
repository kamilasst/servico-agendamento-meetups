package com.womakerscode.microservicemeetup.servico.agendamento.meetups.repository;

import com.womakerscode.microservicemeetup.servico.agendamento.meetups.model.entity.Registration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface RegistrationRepository extends JpaRepository<Registration, Integer> {

    boolean existsByCode(String code);

    Optional<Registration> findByCode(String code);

    @Query(value = "SELECT COUNT(ID) FROM REGISTRATION AS R WHERE R.ID_MEETUP = :ID", nativeQuery = true)
    Integer getCountRegistrationAssociatedMeetup(@Param("ID")Integer id);

}
