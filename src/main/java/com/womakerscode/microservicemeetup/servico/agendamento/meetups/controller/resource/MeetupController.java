package com.womakerscode.microservicemeetup.servico.agendamento.meetups.controller.resource;

import com.womakerscode.microservicemeetup.servico.agendamento.meetups.controller.dto.MeetupDTO;
import com.womakerscode.microservicemeetup.servico.agendamento.meetups.controller.dto.MeetupFilterDTO;
import com.womakerscode.microservicemeetup.servico.agendamento.meetups.controller.dto.RegistrationDTO;
import com.womakerscode.microservicemeetup.servico.agendamento.meetups.model.entity.Meetup;
import com.womakerscode.microservicemeetup.servico.agendamento.meetups.model.entity.Registration;
import com.womakerscode.microservicemeetup.servico.agendamento.meetups.service.MeetupService;
import com.womakerscode.microservicemeetup.servico.agendamento.meetups.service.RegistrationService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/meetups")
@RequiredArgsConstructor
public class MeetupController {

    private final MeetupService meetupService;
//    private final RegistrationService registrationService;
    private final ModelMapper modelMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    private Integer create(@RequestBody MeetupDTO meetupDTO) {

        //TODO ksst - apagar comentários
//        Registration registration = registrationService.getRegistrationByRegistrationAttribute(meetupDTO.getRegistrationAttribute())
//                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST));
        Meetup entity = Meetup.builder()
//                .registration(registration)
                .event(meetupDTO.getEvent())
                .meetupDate("10/10/2021")
                .build();

        entity = meetupService.save(entity);
        return entity.getId();
    }


    @GetMapping
    public Page<MeetupDTO> find(MeetupFilterDTO dto, Pageable pageRequest) {

        Page<Meetup> pageMeetups = meetupService.find(dto, pageRequest);
//        List<MeetupDTO> meetups = pageMeetups
//                .getContent()
//                .stream()
//                .map(meetup -> {
//
////                    Registration registration = meetup.getRegistration();
////                    RegistrationDTO registrationDTO = modelMapper.map(registration, RegistrationDTO.class);
//
//                    MeetupDTO meetupDTO = modelMapper.map(meetup, MeetupDTO.class);
//                  //  meetupDTO.setRegistration(registrationDTO);
//                    return meetupDTO;
//                }).collect(Collectors.toList());

        List<MeetupDTO> meetupsArrayList = new ArrayList<>();
        for (Meetup meetup : pageMeetups.getContent()){
            MeetupDTO meetupDTO = modelMapper.map(meetup, MeetupDTO.class);
            meetupsArrayList.add(meetupDTO);
        }


        return new PageImpl<MeetupDTO>(meetupsArrayList, pageRequest, pageMeetups.getTotalElements());
    }


}
