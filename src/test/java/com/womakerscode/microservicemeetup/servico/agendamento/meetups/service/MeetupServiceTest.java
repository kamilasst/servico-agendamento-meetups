package com.womakerscode.microservicemeetup.servico.agendamento.meetups.service;

import com.womakerscode.microservicemeetup.servico.agendamento.meetups.controller.dto.MeetupDTO;
import com.womakerscode.microservicemeetup.servico.agendamento.meetups.controller.dto.MeetupFilterDTO;
import com.womakerscode.microservicemeetup.servico.agendamento.meetups.exception.BusinessExceptionMessageConstants;
import com.womakerscode.microservicemeetup.servico.agendamento.meetups.exception.BusinessException;
import com.womakerscode.microservicemeetup.servico.agendamento.meetups.model.entity.Meetup;
import com.womakerscode.microservicemeetup.servico.agendamento.meetups.repository.MeetupRepository;
import com.womakerscode.microservicemeetup.servico.agendamento.meetups.service.impl.MeetupServiceImpl;
import com.womakerscode.microservicemeetup.servico.agendamento.meetups.util.MeetupBuilder;
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

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class MeetupServiceTest {

    private MeetupService meetupService;

    @MockBean
    private MeetupRepository meetupRepository;

    @MockBean
    private RegistrationService registrationService;

    @BeforeEach
    public void setUp(){
        this.meetupService = new MeetupServiceImpl(meetupRepository, registrationService);
    }

    @Test
    @DisplayName("Should get an meetup by event")
    public void findByEvent() {

        // arrange
        Meetup meetup = MeetupBuilder.buildJava();

        Mockito.when(meetupRepository.findByEvent(Mockito.any(String.class))).thenReturn(Optional.of(meetup));

        // act
        Optional<Meetup> meetupFound = meetupService.findByEvent(new MeetupFilterDTO("Java"));

        // assert
        assertThat(meetupFound.isPresent()).isTrue();
        Assertions.assertEquals(meetup, meetupFound.get());
    }

    @Test
    @DisplayName("Should get an meetup by id.")
    public void findById() {

        // arrange
        Meetup meetup = MeetupBuilder.buildJava();

        Mockito.when(meetupRepository.findById(1)).thenReturn(Optional.of(meetup));

        // act
        Optional<Meetup> meetupFound = meetupService.findById(1);

        // assert
        assertThat(meetupFound.isPresent()).isTrue();
        Assertions.assertEquals(meetup, meetupFound.get());
    }

    @Test
    @DisplayName("Should find all meetup by page request.")
    public void findByAll() {

        // arrange
        Meetup meetup = MeetupBuilder.buildJava();

        var listMeetups = Arrays.asList(meetup);
        Page<Meetup> page = new PageImpl<>(listMeetups, PageRequest.of(0,10), 0);

        Mockito.when(meetupRepository.findAll(Mockito.any(PageRequest.class))).thenReturn(page);

        // act
        PageRequest pageRequest = PageRequest.of(1,1);
        Page<Meetup> pageReturn = meetupService.findAll(pageRequest);

        // assert
        assertThat(pageReturn.getTotalElements()).isEqualTo(1);
        assertThat(pageReturn.getContent()).isEqualTo(listMeetups);
        assertThat(pageReturn.getPageable().getPageNumber()).isEqualTo(0);
        assertThat(pageReturn.getPageable().getPageSize()).isEqualTo(10);
    }

    @Test
    @DisplayName("Should delete an meetup success")
    public void deleteSuccess(){

        //arrange
        Meetup meetup = MeetupBuilder.buildJava();
        meetup.setId(1);

        //mock
        Mockito.when(meetupRepository.findById(1)).thenReturn(Optional.of(meetup));
        Mockito.when(registrationService.existMeetupOnRegistration(1)).thenReturn(false);

        //act
        meetupService.delete(1);

        //verify
        Mockito.verify(meetupRepository, Mockito.times(1)).delete(meetup);
        Mockito.verify(registrationService, Mockito.times(1)).existMeetupOnRegistration(1);
    }

    @Test
    @DisplayName("Should throws exception meetup not found")
    public void deleteExceptionMeetUpNotFound(){

        //mock
        Mockito.when(meetupRepository.findById(1)).thenReturn(Optional.empty());

        //act
        BusinessException exception = Assertions.assertThrows(BusinessException.class, () -> {
            meetupService.delete(1);
        });

        Assertions.assertEquals(BusinessExceptionMessageConstants.MESSAGE_ERROR_MEETUP_01, exception.getMessage());
    }

    @Test
    @DisplayName("Should throws exception meetup cannot be deleted because Meetup has Registration")
    public void deleteExceptionMeetupCannotBeDeletedBecauseMeetupHasRegistration(){

        //arrange
        Meetup meetup = MeetupBuilder.buildJava();
        meetup.setId(1);

        //mock
        Mockito.when(meetupRepository.findById(1)).thenReturn(Optional.of(meetup));
        Mockito.when(registrationService.existMeetupOnRegistration(1)).thenReturn(true);

        //act
        BusinessException exception = Assertions.assertThrows(BusinessException.class, () -> {
            meetupService.delete(1);
        });

        Assertions.assertEquals(BusinessExceptionMessageConstants.MESSAGE_ERROR_REGISTRATION_01, exception.getMessage());
    }

    @Test
    @DisplayName("Should saved meetup")
    public void SaveSuccess(){

        //arrange
        Meetup meetup = MeetupBuilder.build(null);
        meetup.setId(1);

        //mock
        Mockito.when(meetupRepository.save(Mockito.any(Meetup.class))).thenReturn(meetup);

        //act
        var meetupSavedId = meetupService.save("Java");

        // assert
        Assertions.assertEquals(1, meetupSavedId);
    }

    @Test
    @DisplayName("Should return meetup cannot be null")
    public void SaveExceptionMeetupCannotBeNull(){

        //act
        BusinessException exception = Assertions.assertThrows(BusinessException.class, () -> {
            meetupService.save(null);
        });

        Assertions.assertEquals(BusinessExceptionMessageConstants.MESSAGE_ERROR_MEETUP_02, exception.getMessage());
    }

    @Test
    @DisplayName("Should update meetup")
    public void UpdateSuccess(){

        //arrange
        Meetup meetupJava = MeetupBuilder.build("Java");
        meetupJava.setId(1);

        Meetup meetupUpdated = MeetupBuilder.build("Python");
        meetupJava.setId(1);

        //mock
        Mockito.when(meetupRepository.findById(1)).thenReturn(Optional.of(meetupJava));
        Mockito.when(meetupRepository.save(Mockito.any(Meetup.class))).thenReturn(meetupUpdated);

        //act
        var meetupSaved = meetupService.update(new MeetupDTO(1, "Python"));

        // assert
        Assertions.assertEquals(meetupUpdated, meetupSaved);
    }

    @Test
    @DisplayName("Should update exception")
    public void UpdateExceptionMeetupNull(){

        //mock
        Mockito.when(meetupRepository.findById(1)).thenReturn(Optional.empty());

        //act
        BusinessException exception = Assertions.assertThrows(BusinessException.class, () -> {
            var meetupSaved = meetupService.update(new MeetupDTO(1, "Python"));
        });

        Assertions.assertEquals(BusinessExceptionMessageConstants.MESSAGE_ERROR_MEETUP_01, exception.getMessage());
    }
}
