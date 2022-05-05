package com.womakerscode.microservicemeetup.servico.agendamento.meetups.controller.resource;

import com.womakerscode.microservicemeetup.servico.agendamento.meetups.controller.ApplicationControllerAdvice;
import com.womakerscode.microservicemeetup.servico.agendamento.meetups.controller.dto.MeetupFilterDTO;
import com.womakerscode.microservicemeetup.servico.agendamento.meetups.exception.BusinessException;
import com.womakerscode.microservicemeetup.servico.agendamento.meetups.model.entity.Meetup;
import com.womakerscode.microservicemeetup.servico.agendamento.meetups.model.entity.Registration;
import com.womakerscode.microservicemeetup.servico.agendamento.meetups.controller.dto.RegistrationDTO;
import com.womakerscode.microservicemeetup.servico.agendamento.meetups.service.MeetupService;
import com.womakerscode.microservicemeetup.servico.agendamento.meetups.service.RegistrationService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/registration")
@RequiredArgsConstructor
public class RegistrationController {

    private final RegistrationService registrationService;
    private final MeetupService meetupService;

    @PostMapping("/create")
    public ResponseEntity<RegistrationDTO> create(@RequestBody @Valid RegistrationDTO registrationDTO) {

        try {
            Optional<Meetup> meetupOptional = meetupService.findByEvent(new MeetupFilterDTO(registrationDTO.getEvent()));
            Integer registrationId = registrationService.save(registrationDTO, meetupOptional);

            if (registrationId != null) {
                return ResponseEntity.created(URI.create("/registration/" + registrationId)).build();

            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }

        } catch (BusinessException e) {
            return ApplicationControllerAdvice.handleResponseStatusException(new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage()));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/findByCode/{code}")
    public ResponseEntity<Registration> findByCode(@PathVariable String code) {

        try {

            Optional<Registration> registration = registrationService.findByCode(code);

            if (registration.isPresent()) {
                return ResponseEntity.ok(registration.get());

            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }

        } catch (BusinessException e) {
            return ApplicationControllerAdvice.handleResponseStatusException(new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage()));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/findAll")
    public Page<Registration> findAll(Pageable pageRequest) {

        try {

            Page<Registration> registrationDb = registrationService.findAll(pageRequest);

            List<Registration> registrationList = registrationDb.getContent();

            return new PageImpl<>(registrationList, pageRequest, registrationDb.getTotalElements());

        } catch (BusinessException e) {
            return (Page<Registration>) ApplicationControllerAdvice.handleResponseStatusException(new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage()));

        } catch (Exception e) {
            return (Page<Registration>) ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/findGrouped")
    public ResponseEntity<Map<Meetup, List<Registration>>> findGrouped(@RequestBody RegistrationDTO registrationDTO) {

        try {
            Map<Meetup, List<Registration>> registrationGrouped = registrationService.findGrouped(registrationDTO.getEvent());

            return ResponseEntity.ok(registrationGrouped);

        } catch (BusinessException e) {
            return ApplicationControllerAdvice.handleResponseStatusException(new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage()));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/update")
    public ResponseEntity<Registration> update(@RequestBody RegistrationDTO registrationDTO) {

        try {
            Registration registrationUpdated = registrationService.update(registrationDTO);

            return ResponseEntity.ok(registrationUpdated);

        } catch (BusinessException e) {
            return ApplicationControllerAdvice.handleResponseStatusException(new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage()));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/addMeetupInRegistration")
    public ResponseEntity<Registration> addMeetupInRegistration(@RequestBody RegistrationDTO registrationDTO) {

        try {

            Optional<Meetup> meetupOptional = meetupService.findByEvent(new MeetupFilterDTO(registrationDTO.getEvent()));

            Registration registration = registrationService.addMeetupInRegistration(registrationDTO, meetupOptional);

            return ResponseEntity.ok(registration);

        } catch (BusinessException e) {
            return ApplicationControllerAdvice.handleResponseStatusException(new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage()));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/deleteByCode/{code}")
    public ResponseEntity deleteByCode(@PathVariable String code) {

        try {
            registrationService.delete(code);

            return ResponseEntity.noContent().build();

        } catch (BusinessException e) {
            return ApplicationControllerAdvice.handleResponseStatusException(new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage()));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}

