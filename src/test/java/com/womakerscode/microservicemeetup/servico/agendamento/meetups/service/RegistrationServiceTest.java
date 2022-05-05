package com.womakerscode.microservicemeetup.servico.agendamento.meetups.service;

import com.womakerscode.microservicemeetup.servico.agendamento.meetups.controller.dto.RegistrationDTO;
import com.womakerscode.microservicemeetup.servico.agendamento.meetups.exception.BusinessException;
import com.womakerscode.microservicemeetup.servico.agendamento.meetups.exception.BusinessExceptionMessageConstants;
import com.womakerscode.microservicemeetup.servico.agendamento.meetups.model.entity.Meetup;
import com.womakerscode.microservicemeetup.servico.agendamento.meetups.model.entity.Registration;
import com.womakerscode.microservicemeetup.servico.agendamento.meetups.repository.RegistrationRepository;

import com.womakerscode.microservicemeetup.servico.agendamento.meetups.service.impl.RegistrationServiceImpl;
import com.womakerscode.microservicemeetup.servico.agendamento.meetups.util.MeetupBuilder;
import com.womakerscode.microservicemeetup.servico.agendamento.meetups.util.RegistrationBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class RegistrationServiceTest {

    public static final String CODE_123 = "123";
    public static final String CODE_456 = "456";

    private RegistrationService registrationService;

    @MockBean
    private RegistrationRepository registrationRepository;

    @BeforeEach
    public void setUp(){

        this.registrationService = new RegistrationServiceImpl(registrationRepository);
    }

    @Test
    @DisplayName("Should save an registration with meetup")
    public void saveRegistrationWithMeetup(){

        //Assange
        Optional<Meetup> meetupOptional = MeetupBuilder.createMeetupOptional("Java");
        RegistrationDTO registrationDTO = RegistrationBuilder.createNewRegistrationDTOWithMeetup(CODE_123, meetupOptional.get().getEvent());
        Registration registration = RegistrationBuilder.createNewRegistrationWithMeetup(registrationDTO, meetupOptional.get());

        Mockito.when(registrationRepository.existsByCode(registrationDTO.getCode())).thenReturn(false);
        Mockito.when(registrationRepository.save(Mockito.any(Registration.class))).thenReturn(registration);

        //Act
        Integer idRegistrationSaved = registrationService.save(registrationDTO, meetupOptional);

        //Assert
        Assertions.assertEquals(1, idRegistrationSaved);
    }

    @Test
    @DisplayName("Should save an registration without Meetup")
    public void saveRegistrationWithoutMeetup(){

        //Assange
        RegistrationDTO registrationDTO = RegistrationBuilder.createNewRegistrationDTOWithMeetup(CODE_123, null);
        Registration registration = RegistrationBuilder.createNewRegistrationWithoutMeetup(registrationDTO.getCode());

        Mockito.when(registrationRepository.existsByCode(registrationDTO.getCode())).thenReturn(false);
        Mockito.when(registrationRepository.save(Mockito.any(Registration.class))).thenReturn(registration);

        //Act
        Integer idRegistrationSaved = registrationService.save(registrationDTO, Optional.empty());

        //Assert
        Assertions.assertEquals(1, idRegistrationSaved);
    }

    @Test
    @DisplayName("Should throw business error when thy to save a new registration with a registration duplicated")
    public void notSaveAsRegistrationDuplicated(){

        //Assange
        Optional<Meetup> meetupOptional = MeetupBuilder.createMeetupOptional("Java");
        RegistrationDTO registrationDTO = RegistrationBuilder.createNewRegistrationDTOWithMeetup(CODE_123, meetupOptional.get().getEvent());
        Registration registration = RegistrationBuilder.createNewRegistrationWithMeetup(registrationDTO, meetupOptional.get());

        Mockito.when(registrationRepository.existsByCode(registrationDTO.getCode())).thenReturn(true);

        //Act
        BusinessException exception = Assertions.assertThrows(BusinessException.class, () -> {
            registrationService.save(registrationDTO, meetupOptional);});

        //Assert
        Assertions.assertEquals("Registration already created", exception.getMessage());
        Mockito.verify(registrationRepository, Mockito.never()).save(registration);
    }

    @Test
    @DisplayName("Should delete an registration with success")
    public void deleteRegistrationWithSuccess(){

        //arrange
        Registration registration = Registration.builder().id(11).code(CODE_123).build();

        //Mock
        Mockito.when(registrationRepository.findByCode(CODE_123)).thenReturn(Optional.of(registration));

        //Act
        assertDoesNotThrow(() -> registrationService.delete(CODE_123));

        //Assert
        Mockito.verify(registrationRepository, Mockito.times(1)).delete(registration);
    }

    @Test
    @DisplayName("Should delete an registration invalid code")
    public void deleteNotRegistrationInvalidCode(){

        //Act
        BusinessException exception = Assertions.assertThrows(BusinessException.class, () -> {
            registrationService.delete(null);
        });

        //Assert
        Assertions.assertEquals(BusinessExceptionMessageConstants.MESSAGE_ERROR_REGISTRATION_04, exception.getMessage());
        Mockito.verify(registrationRepository, Mockito.never()).save(Mockito.any(Registration.class));
    }

    @Test
    @DisplayName("Should not delete an registration with code not found")
    public void deleteNotRegistrationCodeNotFound(){

        //Arrange
        //Mock
        Mockito.when(registrationRepository.findByCode(CODE_123)).thenReturn(Optional.empty());

        //Act
        BusinessException exception = Assertions.assertThrows(BusinessException.class, () -> {
            registrationService.delete(CODE_123);
        });

        //Assert
        Assertions.assertEquals(BusinessExceptionMessageConstants.MESSAGE_ERROR_REGISTRATION_03, exception.getMessage());
    }

    @Test
    @DisplayName("Should update an registration with Success")
    public void updateRegistrationWithSuccess(){

        //Arrange
        Meetup meetup = MeetupBuilder.createJava();
        RegistrationDTO registrationDTO = RegistrationBuilder.createNewRegistrationDTOWithMeetup(CODE_123, "Java");
        Registration registration = RegistrationBuilder.createNewRegistrationWithMeetup(CODE_123, meetup);
        registration.setName("Maria");
        registration.setDateOfRegistration("12/04/22");

        Mockito.when(registrationRepository.findByCode(registrationDTO.getCode())).thenReturn(Optional.of(registration));
        Mockito.when(registrationRepository.save(Mockito.any(Registration.class))).thenReturn(registration);

        //Act
        Registration registrationUpdated = registrationService.update(registrationDTO);

        //Assert
        Assertions.assertEquals(registration, registrationUpdated);
    }

    @Test
    @DisplayName("Should not update a null registration")
    public void UpdateNotANullRegistrationDTO(){

        //Arrange
        Mockito.when(registrationRepository.findByCode(Mockito.isNull())).thenReturn(Optional.empty());

        //Act
        BusinessException exception = Assertions.assertThrows(BusinessException.class, () -> {
            registrationService.update(null);
        });

        //Assert
        Assertions.assertEquals(BusinessExceptionMessageConstants.MESSAGE_ERROR_REGISTRATION_05, exception.getMessage());
        Mockito.verify(registrationRepository, Mockito.never()).delete(Mockito.any(Registration.class));
    }

    @Test
    @DisplayName("Should not update an empty registration ")
    public void UpdateNotEmptyRegistration(){

        //Arrange
        RegistrationDTO registrationDTO = RegistrationBuilder.createNewRegistrationDTOWithMeetup(CODE_123, "Java");

        Mockito.when(registrationRepository.findByCode(Mockito.anyString())).thenReturn(Optional.empty());

        //Act
        BusinessException exception = Assertions.assertThrows(BusinessException.class, () -> {
            registrationService.update(registrationDTO);
        });

        //Assert
        Assertions.assertEquals(BusinessExceptionMessageConstants.MESSAGE_ERROR_REGISTRATION_03, exception.getMessage());
        Mockito.verify(registrationRepository, Mockito.never()).delete(Mockito.any(Registration.class));
    }

    @Test
    @DisplayName("Should get by code the registration without meetup ")
    public void findByCodeRegistrationWithoutMeetup(){

        //Arrange
        String code = CODE_123;
        Registration registration = RegistrationBuilder.createNewRegistrationWithoutMeetup(code);

        Mockito.when(registrationRepository.findByCode(code)).thenReturn(Optional.of(registration));

        //Act
        Optional<Registration> foundRegistration = registrationService.findByCode(code);

        //Assert
        assertThat(foundRegistration.isPresent()).isTrue();
        Assertions.assertEquals(registration, foundRegistration.get());
    }

    @Test
    @DisplayName("Should get by code the registration with meetup")
    public void findByCodeRegistrationWithMeetup(){

        //Arrange
        Meetup meetup = MeetupBuilder.createJava();
        String code = CODE_123;
        Registration registration = RegistrationBuilder.createNewRegistrationWithMeetup(code, meetup);

        Mockito.when(registrationRepository.findByCode(code)).thenReturn(Optional.of(registration));

        //Act
        Optional<Registration> foundRegistration = registrationService.findByCode(code);

        //Assert
        assertThat(foundRegistration.isPresent()).isTrue();
        Assertions.assertEquals(registration, foundRegistration.get());
        Assertions.assertEquals(meetup, foundRegistration.get().getMeetup());
    }

    @Test
    @DisplayName("Should return empty when get an registration by code when doesn't exists")
    public void findByCodeRegistrationNotFound(){

        //Arrange
        String code = CODE_456;
        Mockito.when(registrationRepository.findByCode(code)).thenReturn(Optional.empty());

        //Act
        Optional<Registration> registration = registrationService.findByCode(code);

        //Assert
        assertThat(registration.isPresent()).isFalse();
    }

    @Test
    @DisplayName("Should exist Meetup On Registration")
    public void existMeetupOnRegistration() {

        //Assange
        Integer id = 1;
        Mockito.when(registrationRepository.getCountRegistrationAssociatedMeetup(id)).thenReturn(1);

        //Act
        boolean meetupExist = registrationService.existMeetupOnRegistration(id);

        //Assert
        assertThat(meetupExist).isTrue();
    }

    @Test
    @DisplayName("Should exist not Meetup in Registration")
    public void existNotMeetupInRegistration() {

        //Assange
        Integer id = 1;
        Mockito.when(registrationRepository.getCountRegistrationAssociatedMeetup(id)).thenReturn(0);

        //Act
        boolean meetupExist = registrationService.existMeetupOnRegistration(id);

        //Assert
        assertThat(meetupExist).isFalse();
    }

    @Test
    @DisplayName("Should find all registrations by page request.")
    public void findByAllRegistration(){

        //Assange
        Meetup meetup = MeetupBuilder.createJava();
        Registration registration = RegistrationBuilder.createNewRegistrationWithMeetup(CODE_123, meetup);
        var listRegistration = Arrays.asList(registration);

        Page<Registration> page = new PageImpl(listRegistration, PageRequest.of(0,10), 0);

        Mockito.when(registrationRepository.findAll(Mockito.any(PageRequest.class))).thenReturn(page);

        // act
        PageRequest pageRequest = PageRequest.of(1,1);
        Page<Registration> pageReturn = registrationService.findAll(pageRequest);

        // assert
        assertThat(pageReturn.getTotalElements()).isEqualTo(1);
        assertThat(pageReturn.getContent()).isEqualTo(listRegistration);
        assertThat(pageReturn.getPageable().getPageNumber()).isEqualTo(0);
        assertThat(pageReturn.getPageable().getPageSize()).isEqualTo(10);
    }

    @Test
    @DisplayName("Should add Meetup in Registration")
    public void addMeetupInRegistration() {

        //Assange
        Optional<Meetup> meetupOptional = MeetupBuilder.createMeetupOptional("Java");
        RegistrationDTO registrationDTO = RegistrationBuilder.createNewRegistrationDTOWithoutMeetup(CODE_123);
        registrationDTO.setEvent(meetupOptional.get().getEvent());

        Registration registrationWithoutMeetup = RegistrationBuilder.createNewRegistrationWithoutMeetup(registrationDTO.getCode());
        Registration registrationWithMeetup = RegistrationBuilder.createNewRegistrationWithMeetup(registrationDTO, meetupOptional.get());

        Mockito.when(registrationRepository.findByCode(Mockito.anyString())).thenReturn(Optional.of(registrationWithoutMeetup));
        Mockito.when(registrationService.update(registrationDTO)).thenReturn(registrationWithMeetup);

        //Act
        Registration registrationWithMeetupReturn = registrationService.addMeetupInRegistration(registrationDTO, meetupOptional);

        //Assert
        Assertions.assertEquals(registrationWithMeetup, registrationWithMeetupReturn);
    }

    @Test
    @DisplayName("Should not add Meetup in Registration when empty meetup")
    public void AddNotMeetupToRegistrationWhenEmptyMeetup() {

        //Assange
        RegistrationDTO registrationDTO = RegistrationBuilder.createNewRegistrationDTOWithoutMeetup(CODE_123);

        //Act
        BusinessException exception = Assertions.assertThrows(BusinessException.class, () -> {
            registrationService.addMeetupInRegistration(registrationDTO, Optional.empty());
        });

        //Assert
        Assertions.assertEquals(BusinessExceptionMessageConstants.MESSAGE_ERROR_MEETUP_01, exception.getMessage());
        Mockito.verify(registrationRepository, Mockito.never()).delete(Mockito.any(Registration.class));
    }

    @Test
    @DisplayName("Should not add Meetup when RegistrationDTO code not found")
    public void AddNotMeetupWhenRegistrationDTOCodeNotFound() {

        //Assange
        Optional<Meetup> meetupOptional = MeetupBuilder.createMeetupOptional("Java");
        RegistrationDTO registrationDTO = RegistrationBuilder.createNewRegistrationDTOWithoutMeetup(CODE_123);

        Mockito.when(registrationRepository.findByCode(registrationDTO.getCode())).thenReturn(Optional.empty());

        //Act
        BusinessException exception = Assertions.assertThrows(BusinessException.class, () -> {
            registrationService.addMeetupInRegistration(registrationDTO, meetupOptional);
        });

        //Assert
        Assertions.assertEquals(BusinessExceptionMessageConstants.MESSAGE_ERROR_REGISTRATION_03, exception.getMessage());
        Mockito.verify(registrationRepository, Mockito.never()).delete(Mockito.any(Registration.class));
    }

    @Test
    @DisplayName("Should not add Meetup when RegistrationDTO code not found")
    public void notAddMeetupWhenExistMeetupInRegistration() {

        //Assange
        Optional<Meetup> meetupOptional = MeetupBuilder.createMeetupOptional("Java");
        RegistrationDTO registrationDTO = RegistrationBuilder.createNewRegistrationDTOWithMeetup(CODE_123, meetupOptional.get().getEvent());
        Optional<Registration> registration = RegistrationBuilder.createRegistrationOptional(CODE_123,meetupOptional.get());

        Mockito.when(registrationRepository.findByCode(registrationDTO.getCode())).thenReturn(registration);

        //Act
        BusinessException exception = Assertions.assertThrows(BusinessException.class, () -> {
            registrationService.addMeetupInRegistration(registrationDTO, meetupOptional);
        });

        //Assert
        Assertions.assertEquals(BusinessExceptionMessageConstants.MESSAGE_ERROR_REGISTRATION_06, exception.getMessage());
        Mockito.verify(registrationRepository, Mockito.never()).delete(Mockito.any(Registration.class));
    }
}
