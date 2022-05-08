package com.womakerscode.microservicemeetup.servico.agendamento.meetups.util;

import com.womakerscode.microservicemeetup.servico.agendamento.meetups.model.entity.Meetup;

import java.util.Optional;

public class MeetupBuilder {

    public static Meetup build(String event) {
        return Meetup.builder()
                .event(event)
                .date("23/04/2021")
                .registered(true).build();
    }

    public static Meetup buildJava() {
        return build("Java");
    }

    public static Optional<Meetup> buildOptional(String event){

        Optional<Meetup> meetupOptinal = Optional.of(Meetup.builder().id(1).event(event).date("23/04/2021").registered(true).build());

        return meetupOptinal;
    }
}
