package com.womakerscode.microservicemeetup.servico.agendamento.meetups.model.entity;

import lombok.*;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder
@Entity
public class Registration {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "person_name")
    private String name;

    @Column(name = "date_of_registration")
    private String dateOfRegistration;

    @Column
    private String code;

    @JoinColumn(name = "id_meetup")
    @ManyToOne
    private Meetup meetup;

}
