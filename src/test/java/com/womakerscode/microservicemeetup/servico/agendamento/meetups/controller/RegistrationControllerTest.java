package com.womakerscode.microservicemeetup.servico.agendamento.meetups.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.womakerscode.microservicemeetup.servico.agendamento.meetups.controller.resource.RegistrationController;
import com.womakerscode.microservicemeetup.servico.agendamento.meetups.exception.BusinessException;
import com.womakerscode.microservicemeetup.servico.agendamento.meetups.model.entity.Meetup;
import com.womakerscode.microservicemeetup.servico.agendamento.meetups.model.entity.Registration;
import com.womakerscode.microservicemeetup.servico.agendamento.meetups.controller.dto.RegistrationDTO;
import com.womakerscode.microservicemeetup.servico.agendamento.meetups.service.RegistrationService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)  //SprinExtention - p/ o compilador entender que é uma camada de teste
@ActiveProfiles("test") //Ativa a profile de teste da nossa classe Test
@WebMvcTest(controllers = {RegistrationController.class}) //coloca o que vai no contexto dessa anotação. Inserimos uma camada de contexto controller que estamos trabalhando {define a classe que vai querer esta fazendo isso}
@AutoConfigureMockMvc
public class RegistrationControllerTest {

    static String REGISTRATION_API = "/api/registration"; //nessa String static estou definindo a rota da API, antes de acessar qualquer função do controller tem que acessar essa rota

    @Autowired
    MockMvc mockMvc;

    @MockBean
    private RegistrationService registrationService;

    @Test
    @DisplayName("Should create a registratiom with success")
    public void createRegistrationTest() throws Exception{

        //cenario
        RegistrationDTO registrationDTOBuilder = createNewRegistration();
        Registration savedRegistration = Registration.builder().id(101)
                .name("Kamila Santos").dateOfRegistration("10/10/2021").code("001").build();

        //execucao
        BDDMockito.given(registrationService.save(any(RegistrationDTO.class), any(Optional.class))).willReturn(101);// BDDMockito simula a camada do usuário

        String json = new ObjectMapper().writeValueAsString(registrationDTOBuilder);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(REGISTRATION_API)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);

        // verificacao, assert....
        mockMvc
                .perform(request)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").value(101))
                .andExpect(jsonPath("name").value(registrationDTOBuilder.getName()))
                .andExpect(jsonPath("dateOfRegistration").value(registrationDTOBuilder.getDateOfRegistration()))
                .andExpect(jsonPath("registration").value(registrationDTOBuilder.getCode()));
    }

    @Test
    @DisplayName("Should throw an exception when not have dara enough for the test")
    public void createInvalidRegistrationTest() throws Exception{

        String json = new ObjectMapper().writeValueAsString(new RegistrationDTO());

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(REGISTRATION_API)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);

        mockMvc.perform(request)
                .andExpect(status().isBadRequest());
    }

//    @Test
//    @DisplayName("Should throw an exception when try to create a new registration with an registration already created.")
//    public void createRegistrationWithDuplicatedRegistration() throws Exception {
//
//        RegistrationDTO dto = createNewRegistration();
//        String json  = new ObjectMapper().writeValueAsString(dto);
//
//        BDDMockito.given(registrationService.save(any(Registration.class)))
//                .willThrow(new BusinessException("Registration already created!"));
//
//        MockHttpServletRequestBuilder request  = MockMvcRequestBuilders
//                .post(REGISTRATION_API)
//                .contentType(MediaType.APPLICATION_JSON)
//                .accept(MediaType.APPLICATION_JSON)
//                .content(json);
//
//        mockMvc.perform(request)
//                .andExpect(status().isBadRequest())
//                .andExpect(jsonPath("errors", hasSize(1)))
//                .andExpect(jsonPath("errors[0]").value("Registration already created!"));
//    }

    @Test
    @DisplayName("Should get registration information")
    public void getRegistrationTest() throws Exception{
        String code = "002";

        Registration registration = Registration.builder()
                .code(code)
                .name(createNewRegistration().getName())
                .dateOfRegistration(createNewRegistration().getDateOfRegistration())
                .code(createNewRegistration().getCode()).build();

        BDDMockito.given(registrationService.findByCode(code)).willReturn(Optional.of(registration));

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get(REGISTRATION_API.concat("/" + code))
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(jsonPath("code").value(code))
                .andExpect(jsonPath("name").value(createNewRegistration().getName()))
                .andExpect(jsonPath("dateOfRegistration").value(createNewRegistration().getDateOfRegistration()))
                .andExpect(jsonPath("registration").value(createNewRegistration().getCode()));
    }

    @Test
    @DisplayName("Should return NOT FOUND when the registration doesn't exists")
    public void registrationNotFoundTest() throws Exception{

        BDDMockito.given(registrationService.findByCode(anyString())).willReturn(Optional.empty());

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get(REGISTRATION_API.concat("/" + 1))
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should delete the registration")
    public void deleteRegistration() throws Exception {

        BDDMockito.given(registrationService.findByCode(anyString()))
                .willReturn(Optional.of(Registration.builder().id(11).build()));

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete(REGISTRATION_API.concat("/" + 1))
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Should return resource not found when no registration is found to delete")
    public void deleteNonExistentRegistrationTest() throws Exception {

        BDDMockito.given(registrationService
                .findByCode(anyString())).willReturn(Optional.empty());


        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete(REGISTRATION_API.concat("/" + 1))
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isNotFound());
    }

//    @Test
//    @DisplayName("Should update when registration info")
//    public void updateRegistrationTest() throws Exception {
//
//        Integer id = 11;
//        String json = new ObjectMapper().writeValueAsString(createNewRegistration());
//
//        Registration updatingRegistration =
//                Registration.builder()
//                        .id(id)
//                        .name("Julie Neri")
//                        .dateOfRegistration("10/10/2021")
//                        .code("323")
//                        .build();
//
//        BDDMockito.given(registrationService.findByCode(anyString()))
//                .willReturn(Optional.of(updatingRegistration));
//
//        Registration updatedRegistration =
//                Registration.builder()
//                        .id(id)
//                        .name("Kamila Santos")
//                        .dateOfRegistration("10/10/2021")
//                        .code("323")
//                        .build();
//
//        BDDMockito.given(registrationService
//                        .update(updatingRegistration))
//                .willReturn(updatedRegistration);
//
//        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
//                .put(REGISTRATION_API.concat("/" + 1))
//                .contentType(json)
//                .accept(MediaType.APPLICATION_JSON)
//                .contentType(MediaType.APPLICATION_JSON);
//
//        mockMvc.perform(requestBuilder)
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("id").value(id))
//                .andExpect(jsonPath("name").value(createNewRegistration().getName()))
//                .andExpect(jsonPath("dateOfRegistration").value(createNewRegistration().getDateOfRegistration()))
//                .andExpect(jsonPath("registration").value("323"));
//    }

    @Test
    @DisplayName("Should return 404 when try to update an registration no existent")
    public void updateNonExistentRegistrationTest() throws Exception {

        String json = new ObjectMapper().writeValueAsString(createNewRegistration());
        BDDMockito.given(registrationService.findByCode(anyString()))
                .willReturn(Optional.empty());

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .put(REGISTRATION_API.concat("/" + 1))
                .contentType(json)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should filter registration")
    public void findRegistrationTest() throws Exception {

        Integer id = 11;

        Registration registration = Registration.builder()
                .id(id)
                .name(createNewRegistration().getName())
                .dateOfRegistration(createNewRegistration().getDateOfRegistration())
                .code(createNewRegistration().getCode()).build();

        BDDMockito.given(registrationService.findAll(Mockito.any(Pageable.class)) )
                .willReturn(new PageImpl<>(Arrays.asList(registration), PageRequest.of(0,100), 1));

        // ? = Estou passando um parametro/ % = o dado que vai estar vindo/ s = qualquer coisa que vier depois disso / & = separar os parametros
        String queryString = String.format("?name=%s&dateOfRegistration=%s&page=0&size=100",
                registration.getCode(), registration.getDateOfRegistration());


        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get(REGISTRATION_API.concat(queryString))
                .accept(MediaType.APPLICATION_JSON);

        mockMvc
                .perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(jsonPath("content", Matchers.hasSize(1)))
                .andExpect(jsonPath("totalElements"). value(1))
                .andExpect(jsonPath("pageable.pageSize"). value(100))
                .andExpect(jsonPath("pageable.pageNumber"). value(0));

    }

    private RegistrationDTO createNewRegistration() {
        return RegistrationDTO.builder().id(101)
                .name("Kamila Santos").dateOfRegistration("10/10/2021").code("001").build();
    }

}
