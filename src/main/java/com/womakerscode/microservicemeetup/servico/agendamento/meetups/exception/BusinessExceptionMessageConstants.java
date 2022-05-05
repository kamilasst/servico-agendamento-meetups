package com.womakerscode.microservicemeetup.servico.agendamento.meetups.exception;

public class BusinessExceptionMessageConstants {

    // Meetup
    public static final String MESSAGE_ERROR_MEETUP_01 = "Meetup not found";
    public static final String MESSAGE_ERROR_MEETUP_02 = "Event cannot be null";
    public static final String MESSAGE_ERROR_MEETUP_03 = "Date cannot be null";

    // Registration
    public static final String MESSAGE_ERROR_REGISTRATION_01 = "Meetup cannot be deleted because Meetup has Registration";
    public static final String MESSAGE_ERROR_REGISTRATION_02 = "Registration already created";
    public static final String MESSAGE_ERROR_REGISTRATION_03 = "Registration code not found";
    public static final String MESSAGE_ERROR_REGISTRATION_04 = "Invalid code";
    public static final String MESSAGE_ERROR_REGISTRATION_05 = "Registration code Registration cannot be null";


    private BusinessExceptionMessageConstants() {}
}
