package com.womakerscode.microservicemeetup.servico.agendamento.meetups.exception;

public class BusinessException extends RuntimeException {

    public BusinessException(String s) {
        super(s);
    }
}
