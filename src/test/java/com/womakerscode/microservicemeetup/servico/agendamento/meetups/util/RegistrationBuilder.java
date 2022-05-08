package com.womakerscode.microservicemeetup.servico.agendamento.meetups.util;

import com.womakerscode.microservicemeetup.servico.agendamento.meetups.controller.dto.RegistrationDTO;
import com.womakerscode.microservicemeetup.servico.agendamento.meetups.controller.dto.RegistrationFilterDTO;
import com.womakerscode.microservicemeetup.servico.agendamento.meetups.model.entity.Meetup;
import com.womakerscode.microservicemeetup.servico.agendamento.meetups.model.entity.Registration;

import java.util.Optional;

public class RegistrationBuilder {

    public static Registration build(RegistrationDTO registrationDTO, Meetup meetup) {
        return Registration.builder()
                .name(registrationDTO.getName())
                .dateOfRegistration(registrationDTO.getDateOfRegistration())
                .code(registrationDTO.getCode())
                .meetup(meetup).build();
    }

    public static Registration build(RegistrationFilterDTO registrationFilterDTO, Meetup meetup) {
        return Registration.builder()
                .name(registrationFilterDTO.getName())
                .dateOfRegistration(registrationFilterDTO.getDateOfRegistration())
                .code(registrationFilterDTO.getCode())
                .meetup(meetup).build();
    }

    public static Registration build(String code, Meetup meetup) {
        return Registration.builder()
                .name("kamila Santos")
                .dateOfRegistration("10/10/2021")
                .code(code)
                .meetup(meetup).build();
    }

    public static Registration build(String code) {
        return build(code, null);
    }


    public static RegistrationDTO buildDTO(String code, String event) {
        return RegistrationDTO.builder()
                .id(1)
                .name("kamila Santos")
                .dateOfRegistration("10/10/2021")
                .code(code)
                .event(event).build();
    }

    public static RegistrationFilterDTO buildFilterDTO(String code, String event) {
        return RegistrationFilterDTO.builder()
                .name("kamila Santos")
                .dateOfRegistration("10/10/2021")
                .code(code)
                .event(event).build();
    }

    public static RegistrationDTO buildDTO(String code) {
        return RegistrationDTO.builder()
                .id(1)
                .name("kamila Santos")
                .dateOfRegistration("10/10/2021")
                .code(code)
                .build();
    }

    public static Optional<Registration> BuildOptional(String code, Meetup meetup){

        Optional<Registration> registrationOptional = Optional.of(Registration.builder().id(1).name("Kamila Santos").dateOfRegistration("10/10/21").code(code).meetup(meetup).build());

        return registrationOptional;
    }
}
