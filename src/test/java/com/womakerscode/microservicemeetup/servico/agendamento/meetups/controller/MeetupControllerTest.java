package com.womakerscode.microservicemeetup.servico.agendamento.meetups.controller;

import com.womakerscode.microservicemeetup.servico.agendamento.meetups.controller.dto.MeetupDTO;
import com.womakerscode.microservicemeetup.servico.agendamento.meetups.controller.dto.RegistrationDTO;
import com.womakerscode.microservicemeetup.servico.agendamento.meetups.controller.resource.MeetupController;
import com.womakerscode.microservicemeetup.servico.agendamento.meetups.exception.BusinessException;
import com.womakerscode.microservicemeetup.servico.agendamento.meetups.model.entity.Meetup;
import com.womakerscode.microservicemeetup.servico.agendamento.meetups.model.entity.Registration;
import com.womakerscode.microservicemeetup.servico.agendamento.meetups.service.MeetupService;
import com.womakerscode.microservicemeetup.servico.agendamento.meetups.service.RegistrationService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Optional;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@WebMvcTest(controllers = {MeetupController.class})
@AutoConfigureMockMvc
public class MeetupControllerTest {

    static final String MEETUP_API = "/api/meetups";

    @Autowired
    MockMvc mockMvc;

    @MockBean
    private RegistrationService registrationService;

    @MockBean
    private MeetupService meetupService;

    @Test
    @DisplayName("Should create on a meetup")
    public void createMeetupTest() throws Exception {

 //       MeetupDTO meetupDToBuilder = createNewMeetup();


    }




        // quando enviar uma requisicao pra esse registration precisa ser encontrado um valor que tem esse usuario
//        MeetupDTO dto = MeetupDTO.builder().registrationAttribute("123").event("Womakerscode Dados").build();
//        String json = new ObjectMapper().writeValueAsString(dto);
//
//        Registration registration = Registration.builder().id(11).code("123").build();
//
//        BDDMockito.given(registrationService.findByCode("123")).
//                willReturn(Optional.of(registration));
//
//        Meetup meetup = Meetup.builder().id(11).event("Womakerscode Dados").registration(registration).meetupDate("10/10/2021").build();
//
//        BDDMockito.given(meetupService.save(Mockito.any(Meetup.class))).willReturn(meetup);
//
//        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(MEETUP_API)
//                .accept(MediaType.APPLICATION_JSON)
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(json);
//
//        // Aqui o que retorna é o id do registro no meetup
//        mockMvc.perform(request)
//                .andExpect(status().isCreated())
//                .andExpect(content().string("11"));

    }

//    @Test
//    @DisplayName("Should return error when try to register an a meetup nonexistent")
//    public void invalidRegistrationCreateMeetupTest() throws Exception {
//
//        MeetupDTO dto = MeetupDTO.builder().registrationAttribute("123").event("Womakerscode Dados").build();
//        String json = new ObjectMapper().writeValueAsString(dto);
//
//        BDDMockito.given(registrationService.findByCode("123")).
//                willReturn(Optional.empty());
//
//
//        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(MEETUP_API)
//                .accept(MediaType.APPLICATION_JSON)
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(json);
//
//        mockMvc.perform(request)
//                .andExpect(status().isBadRequest());
//
//    }
//
//    @Test
//    @DisplayName("Should return error when try to register a registration already register on a meetup")
//    public void  meetupRegistrationErrorOnCreateMeetupTest() throws Exception {
//
//        MeetupDTO dto = MeetupDTO.builder().registrationAttribute("123").event("Womakerscode Dados").build();
//        String json = new ObjectMapper().writeValueAsString(dto);
//
//
//        Registration registration = Registration.builder().id(11).name("Ana Neri").code("123").build();
//        BDDMockito.given(registrationService.findByCode("123"))
//                .willReturn(Optional.of(registration));
//
//        // procura na base se ja tem algum registration pra esse meetup
//        BDDMockito.given(meetupService.save(Mockito.any(Meetup.class))).willThrow(new BusinessException("Meetup already enrolled"));
//
//
//        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post(MEETUP_API)
//                .accept(MediaType.APPLICATION_JSON)
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(json);
//
//        mockMvc.perform(requestBuilder)
//                .andExpect(status().isBadRequest());
//    }
//
//    //TODO inserir 2 cenarios de erro e 1 de sucesso
//
//
//    private MeetupDTO createNewMeetup() {
//        return MeetupDTO.builder().id(101).event("Java").build();
//    }
//}
