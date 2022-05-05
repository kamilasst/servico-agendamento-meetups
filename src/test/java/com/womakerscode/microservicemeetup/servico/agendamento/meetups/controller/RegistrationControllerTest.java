package com.womakerscode.microservicemeetup.servico.agendamento.meetups.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.womakerscode.microservicemeetup.servico.agendamento.meetups.controller.dto.MeetupFilterDTO;
import com.womakerscode.microservicemeetup.servico.agendamento.meetups.controller.resource.RegistrationController;
import com.womakerscode.microservicemeetup.servico.agendamento.meetups.exception.BusinessException;
import com.womakerscode.microservicemeetup.servico.agendamento.meetups.exception.BusinessExceptionMessageConstants;
import com.womakerscode.microservicemeetup.servico.agendamento.meetups.model.entity.Meetup;
import com.womakerscode.microservicemeetup.servico.agendamento.meetups.model.entity.Registration;
import com.womakerscode.microservicemeetup.servico.agendamento.meetups.controller.dto.RegistrationDTO;
import com.womakerscode.microservicemeetup.servico.agendamento.meetups.service.MeetupService;
import com.womakerscode.microservicemeetup.servico.agendamento.meetups.service.RegistrationService;
import com.womakerscode.microservicemeetup.servico.agendamento.meetups.util.MeetupBuilder;
import com.womakerscode.microservicemeetup.servico.agendamento.meetups.util.RegistrationBuilder;
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

import java.util.*;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@WebMvcTest(controllers = {RegistrationController.class})
@AutoConfigureMockMvc
public class RegistrationControllerTest {

    static String REGISTRATION_API = "/api/registration";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RegistrationService registrationService;

    @MockBean
    private MeetupService meetupService;

    @Test
    @DisplayName("Should create a registration with meetup with success")
    public void createRegistrationWithMeetup() throws Exception{

        // arrange
        Optional<Meetup> meetupOptional = MeetupBuilder.createMeetupOptional("Java");
        RegistrationDTO registrationDTO = RegistrationBuilder.createNewRegistrationDTOWithMeetup("123", meetupOptional.get().getEvent());
        Registration registrationSaved = RegistrationBuilder.createNewRegistrationWithMeetup("123", meetupOptional.get());
        registrationSaved.setId(1);

        String json = new ObjectMapper().writeValueAsString(registrationDTO);

        BDDMockito.given(meetupService.findByEvent(Mockito.any(MeetupFilterDTO.class))).willReturn(meetupOptional);
        BDDMockito.given(registrationService.save(Mockito.any(RegistrationDTO.class), Mockito.any(Optional.class))).willReturn(registrationSaved.getId());

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(REGISTRATION_API.concat("/create"))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        mockMvc.perform(request)
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("Should create a registration without Meetup with success")
    public void createRegistrationWithoutMeetup() throws Exception{

        // arrange
        RegistrationDTO registrationDTO = RegistrationBuilder.createNewRegistrationDTOWithoutMeetup("123");
        Registration registrationSaved = RegistrationBuilder.createNewRegistrationWithoutMeetup("123");

        String json = new ObjectMapper().writeValueAsString(registrationDTO);

        BDDMockito.given(meetupService.findByEvent(Mockito.any(MeetupFilterDTO.class))).willReturn(null);
        BDDMockito.given(registrationService.save(Mockito.any(RegistrationDTO.class), Mockito.any(Optional.class))).willReturn(registrationSaved.getId());

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(REGISTRATION_API.concat("/create"))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        mockMvc.perform(request)
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("Should return error when create registration and return id null")
    public void createRegistrationErrorTest() throws Exception{

        // arrange
        Optional<Meetup> meetupOptional = MeetupBuilder.createMeetupOptional("Java");
        RegistrationDTO registrationDTO = RegistrationBuilder.createNewRegistrationDTOWithMeetup("123", meetupOptional.get().getEvent());

        String json = new ObjectMapper().writeValueAsString(registrationDTO);

        BDDMockito.given(meetupService.findByEvent(Mockito.any(MeetupFilterDTO.class))).willReturn(meetupOptional);
        BDDMockito.given(registrationService.save(Mockito.any(RegistrationDTO.class), Mockito.any(Optional.class))).willReturn(null);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(REGISTRATION_API.concat("/create"))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        mockMvc.perform(request)
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should error when try to create a new registration with an registration already created.")
    public void createNotRegistrationWithDuplicatedRegistration() throws Exception{

        // arrange
        Optional<Meetup> meetupOptional = MeetupBuilder.createMeetupOptional("Java");
        RegistrationDTO registrationDTO = RegistrationBuilder.createNewRegistrationDTOWithMeetup("123", meetupOptional.get().getEvent());

        BDDMockito.given(meetupService.findByEvent(Mockito.any(MeetupFilterDTO.class))).willReturn(meetupOptional);
        BDDMockito.given(registrationService.save(Mockito.any(RegistrationDTO.class), Mockito.any(Optional.class))).willThrow(new BusinessException(BusinessExceptionMessageConstants.MESSAGE_ERROR_REGISTRATION_02));

        String json = new ObjectMapper().writeValueAsString(registrationDTO);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(REGISTRATION_API.concat("/create"))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        mockMvc.perform(request)
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should update a registration success")
    public void updateRegistrationSuccess() throws Exception {

        RegistrationDTO registrationDTO = RegistrationBuilder.createNewRegistrationDTOWithoutMeetup("123");
        Registration registration = RegistrationBuilder.createNewRegistrationWithoutMeetup("123");
        registration.setName("Maria");
        registration.setDateOfRegistration("21/04/22");

        BDDMockito.given(registrationService.update(Mockito.any(RegistrationDTO.class))).willReturn(registration);

        String jsonRequest = new ObjectMapper().writeValueAsString(registrationDTO);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.put(REGISTRATION_API.concat("/update"))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest);

        String json = new ObjectMapper().writeValueAsString(registration);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().string(json));
    }

    @Test
    @DisplayName("Should return error when try to update registration not found.")
    public void updateRegistrationErrorRegistrationNotFound() throws Exception {

        RegistrationDTO registrationDTO = RegistrationBuilder.createNewRegistrationDTOWithoutMeetup("123");

        BDDMockito.given(registrationService.update(Mockito.any(RegistrationDTO.class))).willThrow(new BusinessException(BusinessExceptionMessageConstants.MESSAGE_ERROR_REGISTRATION_04));

        String jsonRequest = new ObjectMapper().writeValueAsString(registrationDTO);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.put(REGISTRATION_API.concat("/update"))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest);

        mockMvc.perform(request)
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should return error when try to update registration code is null.")
    public void updateRegistrationErrorRegistrationCodeIsNull() throws Exception {

        BDDMockito.given(registrationService.update(Mockito.any(RegistrationDTO.class))).willThrow(new BusinessException(BusinessExceptionMessageConstants.MESSAGE_ERROR_REGISTRATION_05));

        RegistrationDTO registrationDTO = RegistrationBuilder.createNewRegistrationDTOWithoutMeetup(null);
        String json = new ObjectMapper().writeValueAsString(registrationDTO);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.put(REGISTRATION_API.concat("/update"))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        mockMvc.perform(request)
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should not update a registration with registration code empty")
    public void updateRegistrationErrorRegistrationCodeEmpty() throws Exception {

        RegistrationDTO registrationDTO = RegistrationBuilder.createNewRegistrationDTOWithoutMeetup("");

        BDDMockito.given(registrationService.update(Mockito.any(RegistrationDTO.class))).willThrow(new BusinessException(BusinessExceptionMessageConstants.MESSAGE_ERROR_REGISTRATION_04));

        String jsonRequest = new ObjectMapper().writeValueAsString(registrationDTO);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.put(REGISTRATION_API.concat("/update"))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest);

        mockMvc.perform(request)
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should find Registration by code success.")
    public void findByCodeRegistrationSuccess() throws Exception {

        String code = "123";

        Registration registration = RegistrationBuilder.createNewRegistrationWithoutMeetup(code);

        BDDMockito.given(registrationService.findByCode(code)).willReturn(Optional.of(registration));

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get(REGISTRATION_API.concat("/findByCode/").concat(code.toString()))
                .accept(MediaType.APPLICATION_JSON);

        String json = new ObjectMapper().writeValueAsString(registration);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().string(json));
    }

    @Test
    @DisplayName("Should find Registration with meetup by code success.")
    public void findByCodeRegistrationWithMeetupSuccess() throws Exception {

        String code = "123";
        Meetup meetup = MeetupBuilder.create("Java");
        Registration registration = RegistrationBuilder.createNewRegistrationWithMeetup(code,meetup);

        BDDMockito.given(registrationService.findByCode(code)).willReturn(Optional.of(registration));

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get(REGISTRATION_API.concat("/findByCode/").concat(code.toString()))
                .accept(MediaType.APPLICATION_JSON);

        String json = new ObjectMapper().writeValueAsString(registration);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().string(json));
    }

    @Test
    @DisplayName("Should find Registration by code.")
    public void findByCodeErrorRegistrationNotFound() throws Exception {

        String code = "123";

        BDDMockito.given(registrationService.findByCode(code)).willThrow(new BusinessException(BusinessExceptionMessageConstants.MESSAGE_ERROR_REGISTRATION_03));

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get(REGISTRATION_API.concat("/findByCode/").concat(code.toString()))
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should not find Registration by code empty.")
    public void findByCodeErrorRegistrationCodeIsEmpty() throws Exception {

        String code = "";

        BDDMockito.given(registrationService.findByCode(Mockito.anyString())).willThrow(new BusinessException(BusinessExceptionMessageConstants.MESSAGE_ERROR_REGISTRATION_04));

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get(REGISTRATION_API.concat("/findByCode/").concat(code))
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should delete the registration with Success")
    public void deleteRegistrationWithSuccess() throws Exception {

        String code = "123";
        BDDMockito.willDoNothing().given(registrationService).delete(code);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete(REGISTRATION_API.concat("/deleteByCode/")
                .concat(code.toString()))
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Should not delete the registration not found")
    public void deleteErrorRegistrationNotFound() throws Exception {

        String code = "123";
        BDDMockito.willThrow(new BusinessException(BusinessExceptionMessageConstants.MESSAGE_ERROR_REGISTRATION_03)).given(registrationService).delete(code);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete(REGISTRATION_API.concat("/deleteByCode/")
                        .concat(code.toString()))
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should not delete the registration not found")
    public void deleteErrorRegistrationWithRegistrationCodeEmpty() throws Exception {

        String code = "";
        BDDMockito.willThrow(new BusinessException(BusinessExceptionMessageConstants.MESSAGE_ERROR_REGISTRATION_04)).given(registrationService).delete(code);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete(REGISTRATION_API.concat("/deleteByCode/")
                        .concat(code.toString()))
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should add Meetup Im Registration with Success")
    public void addMeetupInRegistrationSuccess() throws Exception {

        String event = "Java";
        Optional<Meetup> meetupOptional = MeetupBuilder.createMeetupOptional(event);
        RegistrationDTO registrationDTO = RegistrationBuilder.createNewRegistrationDTOWithoutMeetup("123");
        Registration registration = RegistrationBuilder.createNewRegistrationWithMeetup(registrationDTO.getCode(), meetupOptional.get());

        BDDMockito.given(meetupService.findByEvent(Mockito.any(MeetupFilterDTO.class))).willReturn(meetupOptional);
        BDDMockito.given(registrationService.addMeetupInRegistration(Mockito.any(RegistrationDTO.class), Mockito.any(Optional.class))).willReturn(registration);

        String jsonRequest = new ObjectMapper().writeValueAsString(registrationDTO);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.put(REGISTRATION_API.concat("/addMeetupInRegistration"))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest);

        String json = new ObjectMapper().writeValueAsString(registration);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().string(json));
    }

    @Test
    @DisplayName("Should not add Meetup Im Registration when Meetup already exist In Registration")
    public void notAddMeetupInRegistrationWhenMeetupAlreadyExistINRegistration() throws Exception {

        String event = "Java";
        Optional<Meetup> meetupOptional = MeetupBuilder.createMeetupOptional(event);
        RegistrationDTO registrationDTO = RegistrationBuilder.createNewRegistrationDTOWithoutMeetup("123");

        BDDMockito.given(meetupService.findByEvent(Mockito.any(MeetupFilterDTO.class))).willReturn(meetupOptional);
        BDDMockito.given(registrationService.addMeetupInRegistration(Mockito.any(RegistrationDTO.class), Mockito.any(Optional.class))).willThrow(new BusinessException(BusinessExceptionMessageConstants.MESSAGE_ERROR_REGISTRATION_06));

        String jsonRequest = new ObjectMapper().writeValueAsString(registrationDTO);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.put(REGISTRATION_API.concat("/addMeetupInRegistration"))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest);

        mockMvc.perform(request)
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should all registrations with success")
    public void findAllSuccess() throws Exception {

        Integer id = 11;

        Registration registration = Registration.builder()
                .id(id)
                .name("Kamila")
                .dateOfRegistration("10/10/2022")
                .code("001").build();

        BDDMockito.given(registrationService.findAll(Mockito.any(Pageable.class)) )
                .willReturn(new PageImpl<Registration>(Arrays.asList(registration), PageRequest.of(0,100), 1));

        String queryString = String.format("/findAll?name=%s&dateOfRegistration=%s&page=0&size=100",
                "001", registration.getDateOfRegistration());

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

    @Test
    @DisplayName("Should find grouped filter event.")
    public void findGroupedFilterByEvent() throws Exception {

        String eventJava = "Java";
        Meetup meetupJava = Meetup.builder().id(1).event(eventJava).date("10/10/2022").registered(true).build();
        Registration registrationJava = Registration.builder().id(101)
                .name("Kamila Santos").dateOfRegistration("10/10/2021").code("001").meetup(meetupJava).build();

        Map<Meetup, List<Registration>> mapGroupedFilterJava = new HashMap<>();
        mapGroupedFilterJava.put(meetupJava, Arrays.asList(registrationJava));

        BDDMockito.given(registrationService.findGrouped(eventJava)).willReturn(mapGroupedFilterJava);

        RegistrationDTO dto = RegistrationDTO.builder().event(eventJava).build();

        String jsonRequest = new ObjectMapper().writeValueAsString(dto);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get(REGISTRATION_API.concat("/findGrouped"))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest);

        String jsonResponse = new ObjectMapper().writeValueAsString(mapGroupedFilterJava);
        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().string(jsonResponse));
    }

    @Test
    @DisplayName("Should find grouped all registrations.")
    public void findGroupedAllRegistrations() throws Exception {

        String eventJava = "Java";
        Meetup meetupJava = Meetup.builder().id(1).event(eventJava).date("10/10/2022").registered(true).build();
        Registration registrationJava = Registration.builder().id(101)
                .name("Kamila Santos").dateOfRegistration("10/10/2021").code("001").meetup(meetupJava).build();

        Meetup meetupPython = Meetup.builder().id(2).event(eventJava).date("10/11/2022").registered(true).build();
        Registration registrationPython = Registration.builder().id(102)
                .name("Alan dos Santos").dateOfRegistration("10/11/2021").code("002").meetup(meetupPython).build();

        Map<Meetup, List<Registration>> mapGroupedAll = new HashMap<>();
        mapGroupedAll.put(meetupJava, Arrays.asList(registrationJava));
        mapGroupedAll.put(meetupPython, Arrays.asList(registrationPython));

        BDDMockito.given(registrationService.findGrouped(null)).willReturn(mapGroupedAll);

        String jsonRequest = new ObjectMapper().writeValueAsString(new RegistrationDTO());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get(REGISTRATION_API.concat("/findGrouped"))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest);

        String jsonResponse = new ObjectMapper().writeValueAsString(mapGroupedAll);
        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().string(jsonResponse));
    }
}
