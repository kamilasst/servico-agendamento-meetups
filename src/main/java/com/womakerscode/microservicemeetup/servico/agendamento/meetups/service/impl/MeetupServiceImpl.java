package com.womakerscode.microservicemeetup.servico.agendamento.meetups.service.impl;

import com.womakerscode.microservicemeetup.servico.agendamento.meetups.controller.dto.MeetupDTO;
import com.womakerscode.microservicemeetup.servico.agendamento.meetups.controller.dto.MeetupFilterDTO;
import com.womakerscode.microservicemeetup.servico.agendamento.meetups.controller.dto.MeetupRegistrationDTO;
import com.womakerscode.microservicemeetup.servico.agendamento.meetups.exception.BusinessException;
import com.womakerscode.microservicemeetup.servico.agendamento.meetups.model.entity.Meetup;
import com.womakerscode.microservicemeetup.servico.agendamento.meetups.repository.MeetupRepository;
import com.womakerscode.microservicemeetup.servico.agendamento.meetups.service.MeetupService;
import com.womakerscode.microservicemeetup.servico.agendamento.meetups.service.RegistrationService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class MeetupServiceImpl implements MeetupService {

    private MeetupRepository meetupRepository;

    private RegistrationService registrationService;

    public MeetupServiceImpl(MeetupRepository repository, RegistrationService registrationService) {

        this.meetupRepository = repository;
        this.registrationService = registrationService;
    }

    @Override
    public Meetup save(Meetup meetup) {

        validate(meetup);

        return meetupRepository.save(meetup);
    }

    @Override
    public Integer save(String event) {

        Meetup meetup = Meetup.builder()
                .event(event)
                .date(LocalDate.now().toString())
                .registered(true)
                .build();

        Meetup meetupBd = save(meetup);

        return meetupBd.getId();
    }

    @Override
    public Optional<Meetup> findById(Integer id) {
        return meetupRepository.findById(id);
    }

    @Override
    public Meetup update(Meetup meetup) {
        return meetupRepository.save(meetup);
    }

    @Override
    public Meetup update(MeetupDTO meetupDto) {

        Optional<Meetup> meetupOptional = findById(meetupDto.getId());

        validate(meetupOptional);

        Meetup meetup = meetupOptional.get();
        meetup.setEvent(meetupDto.getEvent());
        Meetup meetupUpdated = update(meetup);

        return meetupUpdated;
    }

    @Override
    public void delete(Integer id) {

        Optional<Meetup> meetup = findById(id);

        validateDelete(meetup);

        this.meetupRepository.delete(meetup.get());
    }

    private void validateDelete(Optional<Meetup> meetup) {

        if (meetup.isEmpty()){
            throw new BusinessException("Meetup not found");
        }

        if(registrationService.existMeetupOnRegistration(meetup.get().getId())){
            throw new BusinessException("Meetup cannot be deleted because Meetup has Registration");
        }
    }

    @Override
    public Optional<Meetup> findByEvent(MeetupFilterDTO filterDTO) {
        return meetupRepository.findByEvent(filterDTO.getEvent());
    }

    @Override
    public Page<Meetup> findAll(Pageable pageRequest) {

        Page<Meetup> meetup = meetupRepository.findAll(pageRequest);

        return meetup;
    }


    @Override
    public List<MeetupRegistrationDTO> findAllRegistrationOnMeetup(MeetupFilterDTO meetupFilterDTO) {
        List<MeetupRegistrationDTO> registrationOnMeetupDto = meetupRepository.findAllRegistrationOnMeetup(meetupFilterDTO.getEvent());
        return registrationOnMeetupDto;
    }

    private void validate(Optional<Meetup> meetupOptional){

        if(meetupOptional.isEmpty()){
            throw new BusinessException("Meetup not found");
        }

        validate(meetupOptional.get());
    }

    private void validate(Meetup meetup){

        if(meetup == null){
            throw new BusinessException("Meetup not found");
        }

        if(StringUtils.isBlank(meetup.getEvent())){
            throw new BusinessException("Event cannot be null");
        }

        if (StringUtils.isBlank(meetup.getDate())) {
            throw new BusinessException("Date cannot be null");
        }
    }
}
