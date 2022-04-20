package com.womakerscode.microservicemeetup.servico.agendamento.meetups.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "tb_registration")
public class Registration {

    @Id //indica que é um id
    @Column(name = "registration_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY) //gera o id automaticamente
    private Integer id;

    @Column(name = "person_name")
    private String name;

    @Column(name = "date_of_registration")
    private String dateOfRegistration;

    @Column
    private String registration;
}
