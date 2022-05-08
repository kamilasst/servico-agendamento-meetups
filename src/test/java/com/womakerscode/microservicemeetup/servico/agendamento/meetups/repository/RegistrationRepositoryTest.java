package com.womakerscode.microservicemeetup.servico.agendamento.meetups.repository;

import com.womakerscode.microservicemeetup.servico.agendamento.meetups.model.entity.Meetup;
import com.womakerscode.microservicemeetup.servico.agendamento.meetups.model.entity.Registration;
import com.womakerscode.microservicemeetup.servico.agendamento.meetups.util.MeetupBuilder;
import com.womakerscode.microservicemeetup.servico.agendamento.meetups.util.RegistrationBuilder;
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
public class RegistrationRepositoryTest {

    private static final String CODE_123 = "123";
    private static final String CODE_789 = "789";
    private static final String CODE_369 = "369";

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private RegistrationRepository registrationRepository;

    @Test
    @DisplayName("Should return true when exists an registration already created.")
    public void existsByCodeReturnTrueWhenRegistrationExists() {

        // arrange
        Registration registration = RegistrationBuilder.build(CODE_123);
        entityManager.persist(registration);

        // act
        boolean exists = registrationRepository.existsByCode(CODE_123);

        // assert
        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("Should return false when doesn't exists an code.")
    public void existsByCodeReturnFalseWhenRegistrationDoesntExists() {

        // act
        boolean exists = registrationRepository.existsByCode(CODE_123);

        // assert
        assertThat(exists).isFalse();
    }

    @Test
    @DisplayName("Should return false when doesn't exists an code with a registration already created.")
    public void existsByCodeReturnFalseWhenDoesntExistsAnCodeWithARegistrationAlreadyCreated() {

        // arrange
        Registration registration = RegistrationBuilder.build(CODE_123);
        entityManager.persist(registration);

        String codeNoExist = "245";

        // act
        boolean exists = registrationRepository.existsByCode(codeNoExist);

        // assert
        assertThat(exists).isFalse();
    }

    @Test
    @DisplayName("Should return false When Registration code is null")
    public void existsByCodeReturnFalseWhenRegistrationCodeIsNull() {

        // act
        boolean exists = registrationRepository.existsByCode(null);

        // assert
        assertThat(exists).isFalse();
    }

    @Test
    @DisplayName("Should return Null When code Registration is null")
    public void existsByCodeReturnFalseWhenRegistrationIsEmpty() {

        // act
        boolean exists = registrationRepository.existsByCode("");

        // assert
        assertThat(exists).isFalse();
    }

    @Test
    @DisplayName("Should get an registration by code without meetup")
    public void findByCodeWithoutMeetup() {

        // arrange
        Registration registration = RegistrationBuilder.build(CODE_123);
        entityManager.persist(registration);

        // act
        Optional<Registration> foundRegistration = registrationRepository
                .findByCode(CODE_123);

        // assert
        assertThat(foundRegistration.isPresent()).isTrue();
        Assertions.assertEquals(registration,foundRegistration.get());
    }

    @Test
    @DisplayName("Should get an registration by code with two registrations already created.")
    public void findByCodeWithTwoRegistrationsAlreadyCreated() {

        // arrange
        Registration registrationCode123 = RegistrationBuilder.build(CODE_123);
        Registration registrationCode789 = RegistrationBuilder.build(CODE_789);
        entityManager.persist(registrationCode123);
        entityManager.persist(registrationCode789);

        // act
        Optional<Registration> foundRegistration = registrationRepository
                .findByCode(CODE_789);

        // assert
        assertThat(foundRegistration.isPresent()).isTrue();
        Assertions.assertEquals(registrationCode789,foundRegistration.get());
    }

    @Test
    @DisplayName("Should return empty when get an registration by code empty.")
    public void findByCodeReturnEmptyWhenFindAnRegistrationByCodeEmpty() {

        // arrange
        Registration registrationCode123 = RegistrationBuilder.build(CODE_123);
        entityManager.persist(registrationCode123);

        // act
        Optional<Registration> foundRegistration = registrationRepository
                .findByCode("");

        // assert
        assertThat(foundRegistration.isPresent()).isFalse();
    }

    @Test
    @DisplayName("Should return empty when get an registration by code null.")
    public void findByCodeReturnEmptyWhenFindAnRegistrationByCodeNull() {

        // arrange
        Registration registrationCode123 = RegistrationBuilder.build(CODE_123);
        entityManager.persist(registrationCode123);

        // act
        Optional<Registration> foundRegistration = registrationRepository
                .findByCode(null);

        // assert
        assertThat(foundRegistration.isPresent()).isFalse();
    }

    @Test
    @DisplayName("Should return empty when get an registration by by code that doesn't exist")
    public void findByCodeReturnEmptyWhenFindAnRegistrationByCodeDoesNotExist() {

        // act
        Optional<Registration> foundRegistration = registrationRepository
                .findByCode(CODE_123);

        // assert
        assertThat(foundRegistration.isPresent()).isFalse();
    }

    @Test
    @DisplayName("Should get an registration by code with meetup")
    public void findByCodeWithMeetup() {

        // arrange
        Meetup meetup = MeetupBuilder.build("Java");
        entityManager.persist(meetup);
        Registration registration = RegistrationBuilder.build(CODE_123, meetup);
        entityManager.persist(registration);

        // act
        Optional<Registration> foundRegistration = registrationRepository
                .findByCode(CODE_123);

        // assert
        assertThat(foundRegistration.isPresent()).isTrue();
        Assertions.assertEquals(registration,foundRegistration.get());
        Assertions.assertEquals(registration.getMeetup(),foundRegistration.get().getMeetup());
    }

    @Test
    @DisplayName("Should get an registration by code with two registrations with meetup already created.")
    public void findByCodeWithTwoRegistrationsWithMeetupAlreadyCreated() {

        // arrange
        Meetup meetupJava = MeetupBuilder.buildJava();
        entityManager.persist(meetupJava);
        Registration registrationCode123 = RegistrationBuilder.build(CODE_123, meetupJava);
        Registration registrationCode789 = RegistrationBuilder.build(CODE_789, meetupJava);
        entityManager.persist(registrationCode123);
        entityManager.persist(registrationCode789);

        // act
        Optional<Registration> foundRegistration = registrationRepository
                .findByCode(CODE_789);

        // assert
        assertThat(foundRegistration.isPresent()).isTrue();
        Assertions.assertEquals(registrationCode789,foundRegistration.get());
        Assertions.assertEquals(registrationCode789.getMeetup(), foundRegistration.get().getMeetup());
    }

    @Test
    @DisplayName("Should save an registration")
    public void saveRegistrationWithoutMeetup() {

        //arrange
        Registration registration = RegistrationBuilder.build(CODE_123);

        //act
        Registration savedRegistration = registrationRepository.save(registration);

        //assert
        assertThat(savedRegistration.getId()).isNotNull();
        Assertions.assertEquals(savedRegistration, registration);

    }

    @Test
    @DisplayName("Should save an registration")
    public void saveRegistrationWithMeetup() {

        //arrange
        Meetup meetup = MeetupBuilder.build("Java");
        entityManager.persist(meetup);
        Registration registration = RegistrationBuilder.build(CODE_123, meetup);
        entityManager.persist(registration);

        Registration savedRegistration = registrationRepository.save(registration);

        assertThat(savedRegistration.getId()).isNotNull();
        Assertions.assertEquals(savedRegistration, registration);

    }

    @Test
    @DisplayName("Should delete and registration from the base")
    public void deleteRegistration() {

        //arrange
        Registration registration = RegistrationBuilder.build(CODE_123);
        entityManager.persist(registration);

        Registration foundRegistration = entityManager
                .find(Registration.class, registration.getId());
        //act
        registrationRepository.delete(foundRegistration);

        Registration deleteRegistration = entityManager
                .find(Registration.class, registration.getId());

        //assert
        assertThat(deleteRegistration).isNull();
    }

    @Test
    @DisplayName("Should get Registration Count Associated with the Meetup")
    public void getCountRegistrationAssociatedMeetup(){

        //Arrange
        Meetup meetupJava = MeetupBuilder.buildJava();
        entityManager.persist(meetupJava);

        Meetup meetupPyton = MeetupBuilder.build("Pyton");
        entityManager.persist(meetupPyton);

        Registration registrationPyton = RegistrationBuilder.build(CODE_789, meetupPyton);
        entityManager.persist(registrationPyton);

        Registration registrationJava = RegistrationBuilder.build(CODE_369, meetupJava);
        entityManager.persist(registrationJava);

        Registration registrationWithoutMeetup = RegistrationBuilder.build(CODE_123);
        entityManager.persist(registrationWithoutMeetup);

        //act
        Integer countRegistration = registrationRepository.getCountRegistrationAssociatedMeetup(meetupPyton.getId());

        //assert
        Assertions.assertEquals(1, countRegistration);
    }

    @Test
    @DisplayName("Should get Registration Zero Count Associated with the Meetup")
    public void getCountRegistrationAssociatedMeetupZeroAssociatedWithTheMeetup(){

        //Arrange
        Meetup meetupJava = MeetupBuilder.buildJava();
        entityManager.persist(meetupJava);

        Meetup meetupPyton = MeetupBuilder.build("Pyton");
        entityManager.persist(meetupPyton);

        Registration registrationPyton = RegistrationBuilder.build(CODE_789, meetupPyton);
        entityManager.persist(registrationPyton);

        Registration registrationPyton2 = RegistrationBuilder.build(CODE_369, meetupPyton);
        entityManager.persist(registrationPyton2);

        Registration registrationWithoutMeetup = RegistrationBuilder.build(CODE_123);
        entityManager.persist(registrationWithoutMeetup);

        //act
        Integer countRegistration = registrationRepository.getCountRegistrationAssociatedMeetup(meetupJava.getId());

        //assert
        Assertions.assertEquals(0, countRegistration);
    }
}
