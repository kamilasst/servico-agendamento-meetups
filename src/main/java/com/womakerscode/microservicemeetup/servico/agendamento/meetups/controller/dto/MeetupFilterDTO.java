package com.womakerscode.microservicemeetup.servico.agendamento.meetups.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MeetupFilterDTO {

    private  String event;
}
