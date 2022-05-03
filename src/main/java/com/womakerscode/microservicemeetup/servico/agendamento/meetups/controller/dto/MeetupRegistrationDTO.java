package com.womakerscode.microservicemeetup.servico.agendamento.meetups.controller.dto;

import com.womakerscode.microservicemeetup.servico.agendamento.meetups.model.entity.Meetup;
import com.womakerscode.microservicemeetup.servico.agendamento.meetups.model.entity.Registration;

import java.util.List;

public class MeetupRegistrationDTO {

    private Meetup meetup;
    private List<Registration> registrations;
}
