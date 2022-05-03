package com.womakerscode.microservicemeetup.servico.agendamento.meetups.service;

import com.womakerscode.microservicemeetup.servico.agendamento.meetups.exception.BusinessException;
import com.womakerscode.microservicemeetup.servico.agendamento.meetups.model.entity.Registration;
import com.womakerscode.microservicemeetup.servico.agendamento.meetups.repository.RegistrationRepository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
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

    RegistrationService registrationService;

    @Autowired
    MeetupService meetupService;

    @MockBean
    RegistrationRepository registrationRepository;

//    @BeforeEach
//    public void setUp(){
//
//        this.registrationService = new RegistrationServiceImpl(registrationRepository); //dependência do service e dar um new na mesma
//    }

    @Test
    @DisplayName("Should save an registration")
    public void saveStudentTest(){

        //cenario - mockei o que eu quero que crie - createValidRegistrattion
        Registration registration = createValidRegistrationDefault();

        //Registration registration2 = createValidRegistration(2, "Adler", LocalDate.now(), "002");

        // Mocks
        Mockito.when(registrationRepository.existsByCode(Mockito.anyString())).thenReturn(false);

        Registration registrationReturn = createValidRegistrationDefault();
        Mockito.when(registrationRepository.save(registration)).thenReturn(registrationReturn);

        //execucao - mock o que quero que retorne - createValidRegistrattion
        Registration savedRegistration = registrationService.save(registration);

        //assert
        assertThat(savedRegistration.getId()).isEqualTo(101);

        //Adler
        org.junit.jupiter.api.Assertions.assertEquals(registrationReturn.getId(), savedRegistration.getId());

        assertThat(savedRegistration.getName()).isEqualTo("Kamila Santos");
        assertThat(savedRegistration.getDateOfRegistration()).isEqualTo("01/04/2022");
        assertThat(savedRegistration.getCode()).isEqualTo("001");

    }

    @Test
    @DisplayName("Should throw business error when thy to save a new registration with a registration duplicated")
    public void shouldNotSaveAsRegistrationDuplicatedTest(){

        //cenario
        Registration registration = createValidRegistrationDefault();
        Mockito.when(registrationRepository.existsByCode(Mockito.any())).thenReturn(true); //Mockito.any() - qualquer dado que ele encontre que esteja duplicado ali dentro

        //execução
        //Chamando uma Throwable pq é o que quero que retorne
        Throwable exceptions = Assertions.catchThrowable( () -> registrationService.save(registration));

        //assert
        assertThat(exceptions)
                .isInstanceOf(BusinessException.class)
                .hasMessage("Registration already created");

        //Não mockamos exceções
        //Mockito vai verificar que dentro do repository ele nunca vai salvar um registro que esteja já criado
        Mockito.verify(registrationRepository, Mockito.never()).save(registration); // entra na instância da classe que estamos trazendo mockadae do objeto que queremos buscar esse retorno e verifica se aquilo aconteceu ou nãp

    }

    @Test
    @DisplayName("Should get an registration by Id")
    public void getByRegistrationCodeTest(){

        //cenario
        String code = "11";
        Registration registration = createValidRegistrationDefault();
        registration.setCode(code);

        Mockito.when(registrationRepository.findByCode(code)).thenReturn(Optional.of(registration));//of -Retorna um Optional com o valor não nulo presente especificado.

        //Execucao
        Optional<Registration> foundRegistration = registrationService.findByCode(code);

        //assert
        assertThat(foundRegistration.isPresent()).isTrue(); //isPresent() - Retorna true se houver um valor presente, caso contrário false
        assertThat(foundRegistration.get().getCode()).isEqualTo(code);//get() = Se um valor estiver presente neste Optional, retorna o valor, caso contrário lança NoSuchElementException.
        assertThat(foundRegistration.get().getName()).isEqualTo(registration.getName());
        assertThat(foundRegistration.get().getDateOfRegistration()).isEqualTo(registration.getDateOfRegistration());
        assertThat(foundRegistration.get().getCode()).isEqualTo(registration.getCode());
    }

    @Test
    @DisplayName("Should return empty when get an registration by id when doesn't exists")
    public void registrationNotFoundByCodeTest(){

        //cenario
        String code = "11";
        Mockito.when(registrationRepository.findByCode(code)).thenReturn(Optional.empty());

        //Execucao
        Optional<Registration> registration = registrationService.findByCode(code);

        //Assert
        assertThat(registration.isPresent()).isFalse();
    }

    @Test
    @DisplayName("Should delete an student")
    public void deleteRegistrationTest(){

        Registration registration = Registration.builder().id(11).build();//Builder - é utilizado para buscar, trazer e construir a sua informação

        assertDoesNotThrow(() -> registrationService.delete(registration.getCode()));//assertDoesNotThrow - Garante que não vai estourar uma exceção quando eu fizer ele

        Mockito.verify(registrationRepository, Mockito.times(1)).delete(registration);//times - indica o numero de invocações dessa classe e nesse caso seja invocado uma única vez
    }

    @Test
    @DisplayName("Should update an registration")
    public void updateRegistrationTest(){

        //cenario
        Integer id = 11;
        Registration updatingRegistration = Registration.builder().id(11).build();

        Registration updatedRegistration = createValidRegistrationDefault();
        updatedRegistration.setId(id);

        Mockito.when(registrationRepository.save(updatingRegistration)).thenReturn(updatedRegistration);

        //execucao
        Registration registration = registrationService.update(updatingRegistration);

        //assert
        assertThat(registration.getId()).isEqualTo(updatedRegistration.getId());
        assertThat(registration.getName()).isEqualTo(updatedRegistration.getName());
        assertThat(registration.getDateOfRegistration()).isEqualTo(updatedRegistration.getDateOfRegistration());
        assertThat(registration.getCode()).isEqualTo(updatedRegistration.getCode());
    }

    @Test
    @DisplayName("Should filter registrations must by properties")
    public void findRegistrationTest(){

        //cenario
        Registration registration = createValidRegistrationDefault();
        PageRequest pageRequest = PageRequest.of(0,10);

        List<Registration> listRegistration = Arrays.asList(registration);
        Page<Registration> page = new PageImpl<Registration>(Arrays.asList(registration),
                PageRequest.of(0,10), 1);

        //Execucao
        Mockito.when(registrationRepository.findAll(Mockito.any(Example.class),
                Mockito.any(PageRequest.class))).thenReturn(page);

        Page<Registration> result = registrationService.findAll(pageRequest);

        //assert
        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent()).isEqualTo(listRegistration);
        assertThat(result.getPageable().getPageNumber()).isEqualTo(0);
        assertThat(result.getPageable().getPageSize()).isEqualTo(10);

    }

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

    //Utilizando o padrão builder eu mock os objetos
    private Registration createValidRegistrationDefault() {
        return createValidRegistration(101, "Kamila Santos","01/04/2022", "001");
    }

    private Registration createValidRegistration(Integer id, String name, String date, String code) {
        return Registration.builder() //criando o padrão builder eu mock os meus objetos
                .id(id)
                .name(name)
                .dateOfRegistration("01/04/2022")
                .code(code)
                .build();
    }
}
