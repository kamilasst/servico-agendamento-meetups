package com.womakerscode.microservicemeetup.servico.agendamento.meetups.util;

import com.womakerscode.microservicemeetup.servico.agendamento.meetups.model.entity.Meetup;

import java.util.Optional;

public class MeetupBuilder {

    public static Meetup create(String event) {
        return Meetup.builder()
                .event(event)
                .date("23/04/2021")
                .registered(true).build();
    }

    public static Meetup createJava() {
        return create("Java");
    }

    public static Optional<Meetup> createMeetupOptional(String event){

        Optional<Meetup> meetupOptinal = Optional.of(Meetup.builder().id(1).event(event).date("23/04/2021").registered(true).build());

        return meetupOptinal;
    }
}
