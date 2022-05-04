package com.womakerscode.microservicemeetup.servico.agendamento.meetups.service;

import com.womakerscode.microservicemeetup.servico.agendamento.meetups.controller.dto.RegistrationDTO;
import com.womakerscode.microservicemeetup.servico.agendamento.meetups.exception.BusinessException;
import com.womakerscode.microservicemeetup.servico.agendamento.meetups.model.entity.Meetup;
import com.womakerscode.microservicemeetup.servico.agendamento.meetups.model.entity.Registration;
import com.womakerscode.microservicemeetup.servico.agendamento.meetups.repository.RegistrationRepository;

import com.womakerscode.microservicemeetup.servico.agendamento.meetups.service.impl.RegistrationServiceImpl;
import com.womakerscode.microservicemeetup.servico.agendamento.meetups.util.MeetupBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(SpringExtension.class)
@ActiveProfiles("test") //identificar que é um profile de test
public class RegistrationServiceTest {

    public static final String CODE_123 = "123";
    RegistrationService registrationService;

//    @Autowired
//    MeetupService meetupService;

    @MockBean
    RegistrationRepository registrationRepository;

    @BeforeEach
    public void setUp(){

        this.registrationService = new RegistrationServiceImpl(registrationRepository); //dependência do service e dar um new na mesma
    }

    @Test
    @DisplayName("Should save an registration")
    public void saveRegistrationWithMeetup(){

        //assange
        Optional<Meetup> meetupOptional = MeetupBuilder.createMeetupOptional("Java");
        RegistrationDTO registrationDTO = createNewRegistrationDTOWithMeetup(CODE_123, meetupOptional.get().getEvent());
        Registration registration = createNewRegistrationWithMeetup(registrationDTO, meetupOptional.get());

        Mockito.when(registrationRepository.existsByCode(registrationDTO.getCode())).thenReturn(false);
        Mockito.when(registrationRepository.save(Mockito.any(Registration.class))).thenReturn(registration);

        //act
        Integer idRegistrationSaved = registrationService.save(registrationDTO, meetupOptional);

        //Assert
        Assertions.assertEquals(1, idRegistrationSaved);
    }

    @Test
    @DisplayName("Should save an registration without Meetup")
    public void saveRegistrationWithoutMeetup(){

        //assange
        RegistrationDTO registrationDTO = createNewRegistrationDTOWithMeetup(CODE_123, null);
        Registration registration = createNewRegistrationWithMeetup(registrationDTO, null);

        Mockito.when(registrationRepository.existsByCode(registrationDTO.getCode())).thenReturn(false);
        Mockito.when(registrationRepository.save(Mockito.any(Registration.class))).thenReturn(registration);

        //act
        Integer idRegistrationSaved = registrationService.save(registrationDTO, null);

        //Assert
        Assertions.assertEquals(1, idRegistrationSaved);
    }

//    @Test
//    @DisplayName("Should throw business error when thy to save a new registration with a registration duplicated")
//    public void shouldNotSaveAsRegistrationDuplicatedTest(){
//
//        //cenario
//        Registration registration = createValidRegistrationDefault();
//        Mockito.when(registrationRepository.existsByCode(Mockito.any())).thenReturn(true); //Mockito.any() - qualquer dado que ele encontre que esteja duplicado ali dentro
//
//        //execução
//        //Chamando uma Throwable pq é o que quero que retorne
//        Throwable exceptions = Assertions.catchThrowable( () -> registrationService.save(registration));
//
//        //assert
//        assertThat(exceptions)
//                .isInstanceOf(BusinessException.class)
//                .hasMessage("Registration already created");
//
//        //Não mockamos exceções
//        //Mockito vai verificar que dentro do repository ele nunca vai salvar um registro que esteja já criado
//        Mockito.verify(registrationRepository, Mockito.never()).save(registration); // entra na instância da classe que estamos trazendo mockadae do objeto que queremos buscar esse retorno e verifica se aquilo aconteceu ou nãp
//
//    }

    @Test
    @DisplayName("Should get an registration without meetup by Code ")
    public void getByRegistrationWithoutMeetupCodeTest(){

        //arrange
        String code = "002";
        Registration registration = createNewRegistrationWithoutMeetup(code);

        Mockito.when(registrationRepository.findByCode(code)).thenReturn(Optional.of(registration));//of -Retorna um Optional com o valor não nulo presente especificado.

        //act
        Optional<Registration> foundRegistration = registrationService.findByCode(code);

        //assert
        assertThat(foundRegistration.isPresent()).isTrue();
        Assertions.assertEquals(registration, foundRegistration.get());
    }

    @Test
    @DisplayName("Should get an registration with meetup by Code ")
    public void getByRegistrationWithMeetupCodeTest(){

        //arrange
        Meetup meetup = MeetupBuilder.createJava();
        String code = "002";
        Registration registration = createNewRegistrationWithMeetup(code, meetup);

        Mockito.when(registrationRepository.findByCode(code)).thenReturn(Optional.of(registration));//of -Retorna um Optional com o valor não nulo presente especificado.

        //act
        Optional<Registration> foundRegistration = registrationService.findByCode(code);

        //assert
        assertThat(foundRegistration.isPresent()).isTrue(); //isPresent() - Retorna true se houver um valor presente, caso contrário false
        Assertions.assertEquals(registration, foundRegistration.get());
        Assertions.assertEquals(meetup, foundRegistration.get().getMeetup());
    }
    @Test
    @DisplayName("Should return empty when get an registration by code when doesn't exists")
    public void registrationNotFoundByCodeTest(){

        //arrange
        String code = "11";
        Mockito.when(registrationRepository.findByCode(code)).thenReturn(Optional.empty());

        //act
        Optional<Registration> registration = registrationService.findByCode(code);

        //Assert
        assertThat(registration.isPresent()).isFalse();
    }

//    @Test
//    @DisplayName("Should Exist Meetup On Registration")
//    public void addMeetupOnRegistration(){
//
//        Optional<Meetup> meetupJava = MeetupBuilder.createMeetupOptional();
//
//        Registration registrationWithoutMeetup = createNewRegistrationWithoutMeetup("123");
//        Registration registrationWithMeetup = createNewRegistrationWithMeetup("123", meetupJava.get());
//
//        RegistrationDTO registrationDTO = createNewRegistrationDTOWithMeetup("123", meetupJava.get());
//
//        Mockito.when(registrationRepository.findByCode(Mockito.anyString())).thenReturn(Optional.of(registrationWithoutMeetup));
//        Mockito.when(registrationService.updregistrationWithMeetup))).the
//
//       Registration registration2 = registrationService.addMeetupInRegistration(registrationDTO, meetupJava);
//
//
//
//    }

//    @Test
//    @DisplayName("Should find all registrations")
//    public void findAllRegistrationTest(){
//
//        //arrange
//        Registration registration = createNewRegistrationWithoutMeetup("123");
//        PageRequest pageRequest = PageRequest.of(0,10);
//
//        List<Registration> listRegistration = Arrays.asList(registration);
//        Page<Registration> page = new PageImpl<Registration>(Arrays.asList(registration),
//                PageRequest.of(1,10), 1);
//
//        //act
//        Mockito.when(registrationRepository.findAll(Mockito.any(Example.class),
//                Mockito.any(PageRequest.class))).thenReturn(page);
//
//        Page<Registration> result = registrationService.findAll(pageRequest);
//
//        //assert
//        assertThat(result.getTotalElements()).isEqualTo(1);
//        assertThat(result.getContent()).isEqualTo(listRegistration);
//        assertThat(result.getPageable().getPageNumber()).isEqualTo(0);
//        assertThat(result.getPageable().getPageSize()).isEqualTo(10);
//
//    }



    @Test
    @DisplayName("Should delete an registration success")
    public void deleteRegistrationSuccess(){

        //arrange
        Registration registration = Registration.builder().id(11).code(CODE_123).build();

        //Mock
        Mockito.when(registrationRepository.findByCode(CODE_123)).thenReturn(Optional.of(registration));

        //act
        assertDoesNotThrow(() -> registrationService.delete(CODE_123));

        Mockito.verify(registrationRepository, Mockito.times(1)).delete(registration);
    }

    @Test
    @DisplayName("Should delete an registration invalid code")
    public void deleteRegistrationInvalidCode(){

        BusinessException exception = Assertions.assertThrows(BusinessException.class, () -> {
            registrationService.delete(null);
        });

        Assertions.assertEquals("Invalid code", exception.getMessage());
    }

    @Test
    @DisplayName("Should delete an registration invalid code")
    public void deleteRegistrationCodeNotFound(){

        //Mock
        Mockito.when(registrationRepository.findByCode(CODE_123)).thenReturn(Optional.empty());

        BusinessException exception = Assertions.assertThrows(BusinessException.class, () -> {
            registrationService.delete(CODE_123);
        });

        Assertions.assertEquals("Registration code not found", exception.getMessage());
    }

//    @Test
//    @DisplayName("Should update an registration")
//    public void updateRegistrationTest(){
//
//        //cenario
//        Integer id = 11;
//        Registration updatingRegistration = Registration.builder().id(11).build();
//
//        Registration updatedRegistration = createValidRegistrationDefault();
//        updatedRegistration.setId(id);
//
//        Mockito.when(registrationRepository.save(updatingRegistration)).thenReturn(updatedRegistration);
//
//        //execucao
//        Registration registration = registrationService.update(updatingRegistration);
//
//        //assert
//        assertThat(registration.getId()).isEqualTo(updatedRegistration.getId());
//        assertThat(registration.getName()).isEqualTo(updatedRegistration.getName());
//        assertThat(registration.getDateOfRegistration()).isEqualTo(updatedRegistration.getDateOfRegistration());
//        assertThat(registration.getCode()).isEqualTo(updatedRegistration.getCode());
//    }

//    @Test
//    @DisplayName("Should filter registrations must by properties")
//    public void findRegistrationTest(){
//
//        //cenario
//        Registration registration = createValidRegistrationDefault();
//        PageRequest pageRequest = PageRequest.of(0,10);
//
//        List<Registration> listRegistration = Arrays.asList(registration);
//        Page<Registration> page = new PageImpl<Registration>(Arrays.asList(registration),
//                PageRequest.of(0,10), 1);
//
//        //Execucao
//        Mockito.when(registrationRepository.findAll(Mockito.any(Example.class),
//                Mockito.any(PageRequest.class))).thenReturn(page);
//
//        Page<Registration> result = registrationService.findAll(pageRequest);
//
//        //assert
//        assertThat(result.getTotalElements()).isEqualTo(1);
//        assertThat(result.getContent()).isEqualTo(listRegistration);
//        assertThat(result.getPageable().getPageNumber()).isEqualTo(0);
//        assertThat(result.getPageable().getPageSize()).isEqualTo(10);
//
//    }

    @Test
    @DisplayName("Should get an Registration model by registration attribute") //buscar um Registration model pelo registration Attibute
    public void getRegistrationByregistrationAttibute(){

        //cenario
        String registrationAttribute = "1234";

        Mockito.when(registrationRepository.findByCode(registrationAttribute))
                .thenReturn(Optional.of(Registration.builder().id(11).code(registrationAttribute).build()));

        //Execucao
        Optional<Registration> registration = registrationService.findByCode(registrationAttribute);

        //assert
        assertThat(registration.isPresent()).isTrue();
        assertThat(registration.get().getId()).isEqualTo(11);
        assertThat(registration.get().getCode()).isEqualTo(registrationAttribute);

        Mockito.verify(registrationRepository, Mockito.times(1)).findByCode(registrationAttribute); //Mockito.times(1) - verifico se o meu repository está sendo invocado pelo ao menos uma vez e quero que ele implemente o findByRegistration
    }

//    //Utilizando o padrão builder eu mock os objetos
//    private Registration createValidRegistrationDefault() {
//        return createValidRegistration(101, "Kamila Santos","01/04/2022", "001");
//    }
//
//    private Registration createValidRegistration(Integer id, String name, String date, String code) {
//        return Registration.builder() //criando o padrão builder eu mock os meus objetos
//                .id(id)
//                .name(name)
//                .dateOfRegistration("01/04/2022")
//                .code(code)
//                .build();
//    }
    public static Registration createNewRegistrationWithMeetup(String code, Meetup meetup) {
        return Registration.builder()
                .id(1)
                .name("kamila Santos")
                .dateOfRegistration("10/10/2021")
                .code(code)
                .meetup(meetup).build();
    }

    private static Registration createNewRegistrationWithoutMeetup(String code) {
        return createNewRegistrationWithMeetup(code, null);
    }


    private static RegistrationDTO createNewRegistrationDTOWithMeetup(String code, String event) {
        return RegistrationDTO.builder()
                .id(1)
                .name("kamila Santos")
                .dateOfRegistration("10/10/2021")
                .code(code)
                .event(event).build();
    }

    private static Registration createNewRegistrationWithMeetup(RegistrationDTO registrationDTO, Meetup meetup) {
        return Registration.builder()
                .id(1)
                .name(registrationDTO.getName())
                .dateOfRegistration(registrationDTO.getDateOfRegistration())
                .code(registrationDTO.getCode())
                .meetup(meetup).build();
    }



}
