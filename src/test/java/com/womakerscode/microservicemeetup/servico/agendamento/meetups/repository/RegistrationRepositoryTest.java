package com.womakerscode.microservicemeetup.servico.agendamento.meetups.repository;

import com.womakerscode.microservicemeetup.servico.agendamento.meetups.model.entity.Registration;
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

    @Autowired
    TestEntityManager entityManager;

    @Autowired
    RegistrationRepository registrationRepository;

    @Test
    @DisplayName("Should return true when exists an registration already created.")
    public void returnTrueWhenRegistrationExists() {

        // arrange
        Registration registration = createNewRegistration(CODE_123);
        entityManager.persist(registration);

        // act
        boolean exists = registrationRepository.existsByCode(CODE_123);

        // assert
        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("Should return false when doesn't exists an code.")
    public void returnFalseWhenRegistrationDoesntExists() {

        // act
        boolean exists = registrationRepository.existsByCode(CODE_123);

        // assert
        assertThat(exists).isFalse();
    }

    @Test
    @DisplayName("Should return false when doesn't exists an code with a registration already created.")
    public void returnFalseWhenDoesntExistsAnCodeWithARegistrationAlreadyCreated() {

        // arrange
        Registration registration = createNewRegistration(CODE_123);
        entityManager.persist(registration);

        String codeNoExist = "245";

        // act
        boolean exists = registrationRepository.existsByCode(codeNoExist);

        // assert
        assertThat(exists).isFalse();
    }

    @Test
    @DisplayName("Should return false When Registration code is null")
    public void returnFalseWhenRegistrationCodeIsNull() {

        // act
        boolean exists = registrationRepository.existsByCode(null);

        // assert
        assertThat(exists).isFalse();
    }

    @Test
    @DisplayName("Should return Null When code Registration is null")
    public void returnFalseWhenRegistrationIsEmpty() {

        // act
        boolean exists = registrationRepository.existsByCode("");

        // assert
        assertThat(exists).isFalse();
    }

    @Test
    @DisplayName("Should get an registration by code")
    public void findByCodeTest() {

        // arrange
        Registration registration = createNewRegistration(CODE_123);
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
    public void findAnRegistrationByCodeWithTwoRegistrationsAlreadyCreatedTest() {

        // arrange
        Registration registrationCode123 = createNewRegistration(CODE_123);
        Registration registrationCode789 = createNewRegistration(CODE_789);
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
    public void returnEmptyWhenFindAnRegistrationByCodeEmpty() {

        // arrange
        Registration registrationCode123 = createNewRegistration(CODE_123);
        entityManager.persist(registrationCode123);

        // act
        Optional<Registration> foundRegistration = registrationRepository
                .findByCode("");

        // assert
        assertThat(foundRegistration.isPresent()).isFalse();
    }

    @Test
    @DisplayName("Should return empty when get an registration by code null.")
    public void returnEmptyWhenFindAnRegistrationByCodeNull() {

        // arrange
        Registration registrationCode123 = createNewRegistration(CODE_123);
        entityManager.persist(registrationCode123);

        // act
        Optional<Registration> foundRegistration = registrationRepository
                .findByCode(null);

        // assert
        assertThat(foundRegistration.isPresent()).isFalse();
    }

    @Test
    @DisplayName("Should return empty when get an registration by by code that doesn't exist")
    public void returnEmptyWhenFindAnRegistrationByCodeDoesNotExist() {

        // act
        Optional<Registration> foundRegistration = registrationRepository
                .findByCode(CODE_123);

        // assert
        assertThat(foundRegistration.isPresent()).isFalse();
    }





//
//    @Test
//    @DisplayName("Should save an registration")
//    public void saveRegistrationTest() {
//
//        Registration registration_Class_attribute = createNewRegistration("323");
//
//        Registration savedRegistration = registrationRepository.save(registration_Class_attribute);
//
//        assertThat(savedRegistration.getId()).isNotNull();
//
//    }
//
//    @Test
//    @DisplayName("Should delete and registration from the base")
//    public void deleteRegistation() {
//
//        Registration registration_Class_attribute = createNewRegistration("323");
//        entityManager.persist(registration_Class_attribute);
//
//        Registration foundRegistration = entityManager
//                .find(Registration.class, registration_Class_attribute.getId());
//        registrationRepository.delete(foundRegistration);
//
//        Registration deleteRegistration = entityManager
//                .find(Registration.class, registration_Class_attribute.getId());
//
//        assertThat(deleteRegistration).isNull();
//    }

    public static Registration createNewRegistration(String code) {
        return Registration.builder()
                .name("kamila Santos")
                .dateOfRegistration("10/10/2021")
                .code(code).build();
    }
}
