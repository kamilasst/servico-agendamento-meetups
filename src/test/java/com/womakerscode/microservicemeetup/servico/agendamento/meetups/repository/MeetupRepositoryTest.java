package com.womakerscode.microservicemeetup.servico.agendamento.meetups.repository;

import com.womakerscode.microservicemeetup.servico.agendamento.meetups.model.entity.Meetup;
import com.womakerscode.microservicemeetup.servico.agendamento.meetups.util.MeetupBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest
public class MeetupRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private MeetupRepository meetupRepository;

    @Test
    @DisplayName("Should get an meetup by event.")
    public void findByEvent() {

        // arrange
        Meetup meetupJava = MeetupBuilder.buildJava();
        Meetup meetupPython = MeetupBuilder.build("Python");
        entityManager.persist(meetupJava);
        entityManager.persist(meetupPython);

        // act
        Optional<Meetup> meetupFound = meetupRepository.findByEvent("Java");

        // assert
        assertThat(meetupFound.isPresent()).isTrue();
        Assertions.assertEquals(meetupJava, meetupFound.get());
    }

    @Test
    @DisplayName("Should get an meetup by event not founds")
    public void findByEventNotFound() {

        // arrange
        Meetup meetupJava = MeetupBuilder.buildJava();
        Meetup meetupPython = MeetupBuilder.build("Python");
        entityManager.persist(meetupJava);
        entityManager.persist(meetupPython);

        // act
        Optional<Meetup> meetupFound = meetupRepository.findByEvent("ABC");

        // assert
        assertThat(meetupFound.isEmpty()).isTrue();
    }
}

