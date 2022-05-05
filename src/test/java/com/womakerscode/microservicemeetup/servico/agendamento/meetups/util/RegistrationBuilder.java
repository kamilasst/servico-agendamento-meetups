package com.womakerscode.microservicemeetup.servico.agendamento.meetups.util;

import com.womakerscode.microservicemeetup.servico.agendamento.meetups.controller.dto.RegistrationDTO;
import com.womakerscode.microservicemeetup.servico.agendamento.meetups.model.entity.Meetup;
import com.womakerscode.microservicemeetup.servico.agendamento.meetups.model.entity.Registration;

import java.util.Optional;

public class RegistrationBuilder {

    public static Registration createNewRegistrationWithMeetup(RegistrationDTO registrationDTO, Meetup meetup) {
        return Registration.builder()
                .id(1)
                .name(registrationDTO.getName())
                .dateOfRegistration(registrationDTO.getDateOfRegistration())
                .code(registrationDTO.getCode())
                .meetup(meetup).build();
    }

    public static Registration createNewRegistrationWithMeetup(String code, Meetup meetup) {
        return Registration.builder()
                .id(1)
                .name("kamila Santos")
                .dateOfRegistration("10/10/2021")
                .code(code)
                .meetup(meetup).build();
    }

    public static Registration createNewRegistrationWithoutMeetup(String code) {
        return createNewRegistrationWithMeetup(code, null);
    }


    public static RegistrationDTO createNewRegistrationDTOWithMeetup(String code, String event) {
        return RegistrationDTO.builder()
                .id(1)
                .name("kamila Santos")
                .dateOfRegistration("10/10/2021")
                .code(code)
                .event(event).build();
    }

    public static RegistrationDTO createNewRegistrationDTOWithoutMeetup(String code) {
        return RegistrationDTO.builder()
                .id(1)
                .name("kamila Santos")
                .dateOfRegistration("10/10/2021")
                .code(code)
                .build();
    }

    public static Optional<Registration> createRegistrationOptional(String code, Meetup meetup){

        Optional<Registration> registrationOptional = Optional.of(Registration.builder().id(1).name("Kamila Santos").dateOfRegistration("28/04/22").code(code).meetup(meetup).build());

        return registrationOptional;
    }
}
