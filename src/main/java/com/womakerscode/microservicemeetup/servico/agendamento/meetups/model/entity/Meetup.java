package com.womakerscode.microservicemeetup.servico.agendamento.meetups.model.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Builder
@Entity
public class Meetup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    @NotNull
    private String event;

    @Column
    @NotNull
    private String date;

    @Column
    private Boolean registered;

}
