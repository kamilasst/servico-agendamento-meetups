package com.womakerscode.microservicemeetup.servico.agendamento.meetups.controller;

import com.womakerscode.microservicemeetup.servico.agendamento.meetups.controller.dto.MeetupDTO;
import com.womakerscode.microservicemeetup.servico.agendamento.meetups.controller.dto.MeetupFilterDTO;
import com.womakerscode.microservicemeetup.servico.agendamento.meetups.controller.resource.MeetupController;
import com.womakerscode.microservicemeetup.servico.agendamento.meetups.exception.BusinessException;
import com.womakerscode.microservicemeetup.servico.agendamento.meetups.exception.BusinessExceptionMessageConstants;
import com.womakerscode.microservicemeetup.servico.agendamento.meetups.model.entity.Meetup;
import com.womakerscode.microservicemeetup.servico.agendamento.meetups.service.MeetupService;
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
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@WebMvcTest(controllers = {MeetupController.class})
@AutoConfigureMockMvc
public class MeetupControllerTest {

    private static final String MEETUP_API = "/api/meetups";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MeetupService meetupService;

    @Test
    @DisplayName("Should create on a meetup")
    public void createMeetup() throws Exception {

        // arrange
        MeetupFilterDTO dto = MeetupFilterDTO.builder().event("Java").build();
        Meetup meetupSaved = Meetup.builder().id(101).event("Java").date("05/05/2022").registered(true).build();
        String json = new ObjectMapper().writeValueAsString(dto);

        BDDMockito.given(meetupService.save(Mockito.any(String.class))).willReturn(meetupSaved.getId());

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(MEETUP_API.concat("/create"))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        mockMvc.perform(request)
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("Should return error when try to create meetup not found.")
    public void createMeetupErrorMeetupNotFound() throws Exception {

        MeetupFilterDTO dto = MeetupFilterDTO.builder().event("Java").build();
        String json = new ObjectMapper().writeValueAsString(dto);

        BDDMockito.given(meetupService.save(Mockito.any(String.class))).willThrow(new BusinessException(BusinessExceptionMessageConstants.MESSAGE_ERROR_MEETUP_01));

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post(MEETUP_API.concat("/create"))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should return error when try to create meetup event not found.")
    public void createMeetupErrorEventNotFound() throws Exception {

        MeetupFilterDTO dto = MeetupFilterDTO.builder().event("Java").build();
        String json = new ObjectMapper().writeValueAsString(dto);

        BDDMockito.given(meetupService.save(Mockito.any(String.class))).willThrow(new BusinessException(BusinessExceptionMessageConstants.MESSAGE_ERROR_MEETUP_02));

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post(MEETUP_API.concat("/create"))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should return error when try to create meetup date null.")
    public void createMeetupErrorDateCannotNull() throws Exception {

        MeetupFilterDTO dto = MeetupFilterDTO.builder().event("Java").build();
        String json = new ObjectMapper().writeValueAsString(dto);

        BDDMockito.given(meetupService.save(Mockito.any(String.class))).willThrow(new BusinessException(BusinessExceptionMessageConstants.MESSAGE_ERROR_MEETUP_03));

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post(MEETUP_API.concat("/create"))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should find by id.")
    public void findById() throws Exception {

        Integer id = 1;

        Meetup meetup = Meetup.builder().id(id).event("Java").date("10/10/2022").registered(true).build();

        BDDMockito.given(meetupService.findById(id)).willReturn(Optional.of(meetup));

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get(MEETUP_API.concat("/findById/").concat(id.toString()))
                .accept(MediaType.APPLICATION_JSON);

        String json = new ObjectMapper().writeValueAsString(meetup);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().string(json));
    }

    @Test
    @DisplayName("Should update on a meetup")
    public void updateMeetup() throws Exception {

        Integer id = 1;
        Meetup meetup = Meetup.builder().id(id).event("Java").date("10/10/2022").registered(true).build();
        BDDMockito.given(meetupService.update(Mockito.any(MeetupDTO.class))).willReturn(meetup);

        MeetupDTO dto = MeetupDTO.builder().id(id).event("Java").build();

        String jsonRequest = new ObjectMapper().writeValueAsString(dto);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.put(MEETUP_API.concat("/update"))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest);

        String json = new ObjectMapper().writeValueAsString(meetup);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().string(json));
    }

    @Test
    @DisplayName("Should return error when try to update meetup not found.")
    public void updateMeetupErrorMeetupNotFound() throws Exception {

        BDDMockito.given(meetupService.update(Mockito.any(MeetupDTO.class))).willThrow(new BusinessException(BusinessExceptionMessageConstants.MESSAGE_ERROR_MEETUP_01));

        MeetupDTO dto = MeetupDTO.builder().event("Java").build();
        String json = new ObjectMapper().writeValueAsString(dto);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.put(MEETUP_API.concat("/update"))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should return error when try to update meetup, event cannot be null.")
    public void updateMeetupErrorEventCannotBeNullNotFound() throws Exception {

        BDDMockito.given(meetupService.update(Mockito.any(MeetupDTO.class))).willThrow(new BusinessException(BusinessExceptionMessageConstants.MESSAGE_ERROR_MEETUP_02));

        MeetupDTO dto = MeetupDTO.builder().event("Java").build();
        String json = new ObjectMapper().writeValueAsString(dto);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.put(MEETUP_API.concat("/update"))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should return error when try to update meetup date cannot be null.")
    public void updateMeetupErrorEventDateCannotBeNullNotFound() throws Exception {

        BDDMockito.given(meetupService.update(Mockito.any(MeetupDTO.class))).willThrow(new BusinessException(BusinessExceptionMessageConstants.MESSAGE_ERROR_MEETUP_03));

        MeetupDTO dto = MeetupDTO.builder().event("Java").build();
        String json = new ObjectMapper().writeValueAsString(dto);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.put(MEETUP_API + "/update")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should delete on a meetup")
    public void deleteMeetup() throws Exception {

        Integer id = 1;

        BDDMockito.willDoNothing().given(meetupService).delete(Mockito.any(Integer.class));

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.delete(MEETUP_API.concat("/deleteById/".concat(id.toString())))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Should delete meetup not found")
    public void deleteMeetupNotFound() throws Exception {

        Integer id = 1;

        BDDMockito.willThrow(new BusinessException(BusinessExceptionMessageConstants.MESSAGE_ERROR_MEETUP_01)).given(meetupService).delete(Mockito.any(Integer.class));

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.delete(MEETUP_API.concat("/deleteById/".concat(id.toString())))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request).andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should find all.")
    public void findByAll() throws Exception {

        Integer id = 1;

        Meetup meetup = Meetup.builder().id(id).event("Java").date("10/10/2022").registered(true).build();

        BDDMockito.given(meetupService.findAll(Mockito.any(Pageable.class)))
                .willReturn(new PageImpl<Meetup>(Arrays.asList(meetup), PageRequest.of(0, 100), 1));

        String queryString = String.format("/findAll?name=%s&date=%s&page=0&size=100",
                meetup.getEvent(), meetup.getDate());

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get(MEETUP_API.concat(queryString))
                .accept(MediaType.APPLICATION_JSON);

        mockMvc
                .perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(jsonPath("content", Matchers.hasSize(1)))
                .andExpect(jsonPath("totalElements").value(1))
                .andExpect(jsonPath("pageable.pageSize").value(100))
                .andExpect(jsonPath("pageable.pageNumber").value(0));
    }
}
