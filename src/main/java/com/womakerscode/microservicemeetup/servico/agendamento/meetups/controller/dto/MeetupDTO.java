package com.womakerscode.microservicemeetup.servico.agendamento.meetups.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MeetupDTO {

    private Integer id;
    private String event;

}
