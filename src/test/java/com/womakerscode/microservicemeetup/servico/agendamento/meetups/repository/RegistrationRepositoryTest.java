package com.womakerscode.microservicemeetup.servico.agendamento.meetups.repository;

import com.womakerscode.microservicemeetup.servico.agendamento.meetups.model.entity.Registration;
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

    @Autowired
    TestEntityManager entityManager;

    @Autowired
    RegistrationRepository registrationRepository;

    @Test
    @DisplayName("Should return true when exists an registration already created.")
    public void returnTrueWhenRegistrationExists() {

        String registration = "123";

        Registration registration_attribute = createNewRegistration(registration);
        entityManager.persist(registration_attribute);

        boolean exists = registrationRepository.existsByCode(registration);

        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("Should return false when doesn't exists an registration_attribute with a registration already created.")
    public void returnFalseWhenRegistrationAttributeDoesntExists() {

        String registration = "123";

        boolean exists = registrationRepository.existsByCode(registration);

        assertThat(exists).isFalse();

    }

    @Test
    @DisplayName("Should get an registration by id")
    public void findByIdTest() {

        Registration registration_Class_attribute = createNewRegistration("323");
        entityManager.persist(registration_Class_attribute);

        Optional<Registration> foundRegistration = registrationRepository
                .findById(registration_Class_attribute.getId());

        assertThat(foundRegistration.isPresent()).isTrue();

    }

    @Test
    @DisplayName("Should save an registration")
    public void saveRegistrationTest() {

        Registration registration_Class_attribute = createNewRegistration("323");

        Registration savedRegistration = registrationRepository.save(registration_Class_attribute);

        assertThat(savedRegistration.getId()).isNotNull();

    }

    @Test
    @DisplayName("Should delete and registration from the base")
    public void deleteRegistation() {

        Registration registration_Class_attribute = createNewRegistration("323");
        entityManager.persist(registration_Class_attribute);

        Registration foundRegistration = entityManager
                .find(Registration.class, registration_Class_attribute.getId());
        registrationRepository.delete(foundRegistration);

        Registration deleteRegistration = entityManager
                .find(Registration.class, registration_Class_attribute.getId());

        assertThat(deleteRegistration).isNull();
    }

    public static Registration createNewRegistration(String registration) {
        return Registration.builder()
                .name("kamila Santos")
                .dateOfRegistration("10/10/2021")
                .code(registration).build();
    }
}
