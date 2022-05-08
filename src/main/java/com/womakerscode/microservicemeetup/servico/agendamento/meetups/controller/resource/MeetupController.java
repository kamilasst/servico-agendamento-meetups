package com.womakerscode.microservicemeetup.servico.agendamento.meetups.controller.resource;

import com.womakerscode.microservicemeetup.servico.agendamento.meetups.controller.ApplicationControllerAdvice;
import com.womakerscode.microservicemeetup.servico.agendamento.meetups.controller.dto.MeetupDTO;
import com.womakerscode.microservicemeetup.servico.agendamento.meetups.controller.dto.MeetupFilterDTO;
import com.womakerscode.microservicemeetup.servico.agendamento.meetups.exception.BusinessException;
import com.womakerscode.microservicemeetup.servico.agendamento.meetups.model.entity.Meetup;
import com.womakerscode.microservicemeetup.servico.agendamento.meetups.service.MeetupService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/meetups")
@RequiredArgsConstructor
public class MeetupController {

    private final MeetupService meetupService;

    @PostMapping("/create")
    private ResponseEntity<Integer> create(@RequestBody MeetupFilterDTO meetupFilterDTO) {

        try {
            Integer meetupId = meetupService.save(meetupFilterDTO.getEvent());

            return ResponseEntity.created(URI.create("/meetups/" + meetupId)).build();

        } catch (BusinessException e) {
            return ApplicationControllerAdvice.handleResponseStatusException(new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage()));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/findById/{id}")
    private ResponseEntity<Meetup> findById(@PathVariable Integer id) {

        try {
            Optional<Meetup> meetupOptional = meetupService.findById(id);

            if (meetupOptional.isPresent()) {
                return ResponseEntity.ok(meetupOptional.get());
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
    public Page<Meetup> findAll(Pageable pageRequest) {

        try {

            Page<Meetup> meetupDb = meetupService.findAll(pageRequest);

            List<Meetup> meetupList = meetupDb.getContent();

            return new PageImpl<>(meetupList, pageRequest, meetupDb.getTotalElements());

        } catch (BusinessException e) {
            return (Page<Meetup>) ApplicationControllerAdvice.handleResponseStatusException(new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage()));

        } catch (Exception e) {
            return (Page<Meetup>) ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/update")
    private ResponseEntity<Meetup> update(@RequestBody MeetupDTO meetupDTO) {

        try {
            Meetup meetupUpdated = meetupService.update(meetupDTO);

            return ResponseEntity.ok(meetupUpdated);

        } catch (BusinessException e) {
            return ApplicationControllerAdvice.handleResponseStatusException(new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage()));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/deleteById/{id}")
    private ResponseEntity deleteById(@PathVariable Integer id) {

        try {
            meetupService.delete(id);
            return ResponseEntity.noContent().build();

        } catch (BusinessException e) {
            return ApplicationControllerAdvice.handleResponseStatusException(new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage()));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
