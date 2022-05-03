package com.womakerscode.microservicemeetup.servico.agendamento.meetups.service.impl;

import com.womakerscode.microservicemeetup.servico.agendamento.meetups.controller.dto.RegistrationDTO;
import com.womakerscode.microservicemeetup.servico.agendamento.meetups.exception.BusinessException;
import com.womakerscode.microservicemeetup.servico.agendamento.meetups.model.entity.Meetup;
import com.womakerscode.microservicemeetup.servico.agendamento.meetups.model.entity.Registration;
import com.womakerscode.microservicemeetup.servico.agendamento.meetups.repository.RegistrationRepository;
import com.womakerscode.microservicemeetup.servico.agendamento.meetups.service.RegistrationService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RegistrationServiceImpl implements RegistrationService {

    private RegistrationRepository registrationRepository;

    public RegistrationServiceImpl(RegistrationRepository repository) {
        this.registrationRepository = repository;
    }

    public Registration save(Registration registration) {

        if (registrationRepository.existsByCode(registration.getCode())){
            throw new BusinessException("Registration already created");
        }

        return registrationRepository.save(registration);
    }

    public Integer save(RegistrationDTO registrationDTO, Optional<Meetup> meetupOptional){

        if (meetupOptional.isPresent()) {
            Registration registration = createRegistration(registrationDTO, meetupOptional.get());

            Registration registrationBd = save(registration);

            return registrationBd.getId();
        }
        return null;
    }

    @Override
    public Optional<Registration> findByCode(String code) {

        validateCode(code);
        return registrationRepository.findByCode(code);
    }

    @Override
    public Registration update(Registration registration) {
        return registrationRepository.save(registration);
    }

    @Override
    public Registration update(RegistrationDTO registrationDTO) {

        validateRegistrationDTO(registrationDTO);

        Optional<Registration> registrationBd = findByCode(registrationDTO.getCode());

        validateRegistration(registrationBd);

        Registration registration = registrationBd.get();

        if (registrationDTO.getName() != null) {
            registration.setName(registrationDTO.getName());
        }
        if (registrationDTO.getDateOfRegistration() != null) {
            registration.setDateOfRegistration(registrationDTO.getDateOfRegistration());
        }

        Registration registrationUpdated = update(registration);
        return registrationUpdated;
    }

    private void validateRegistration(Optional<Registration> registrationBd) {
        if (registrationBd.isEmpty()) {
            throw new BusinessException("Registration code not found");
        }
    }

    @Override
    public void delete(String code) {

        validateCode(code);

        Optional<Registration> registrationDb = findByCode(code);

        validateRegistration(registrationDb);

        this.registrationRepository.delete(registrationDb.get());
    }

    @Override
    public Page<Registration> findAll(Pageable pageRequest) {

        Page<Registration> registration = registrationRepository.findAll(pageRequest);
        return registration;
    }

    public Registration addMeetupInRegistration(RegistrationDTO registrationDTO, Optional<Meetup> meetupOptional){

        validateMeetUp(meetupOptional);

        Optional<Registration> registrationOptional = findByCode(registrationDTO.getCode());

        validateRegistration(registrationOptional);

        Registration registration = registrationOptional.get();
        registration.setMeetup(meetupOptional.get());

        Registration registrationUpdated = update(registration);

        return registrationUpdated;
    }

    private void validateMeetUp(Optional<Meetup> meetupOptional) {

        if(meetupOptional.isEmpty()) {
            throw new BusinessException("Meetup not found");
        }
    }

    @Override
    public boolean existMeetupOnRegistration(Integer id){

        Integer count = registrationRepository.getCountRegistrationAssociatedMeetup(id);

        if (count > 0){
            return true;
        }
        return false;
    }

    private void validateCode(String code) {

        if(StringUtils.isBlank(code)){
            throw new BusinessException("Invalid code");
        }
    }

    private void validateRegistrationDTO(RegistrationDTO registrationDTO) {

        if (registrationDTO == null || StringUtils.isBlank(registrationDTO.getCode())) {
            throw new BusinessException("Registration code cannot be null");
        }
    }

    private Registration createRegistration(RegistrationDTO registrationDTO, Meetup meetup){

        return Registration.builder()
                .name(registrationDTO.getName())
                .dateOfRegistration(registrationDTO.getDateOfRegistration())
                .code(registrationDTO.getCode())
                .meetup(meetup)
                .build();
    }
}
