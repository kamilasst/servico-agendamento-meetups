package com.womakerscode.microservicemeetup.servico.agendamento.meetups.service.impl;

import com.womakerscode.microservicemeetup.servico.agendamento.meetups.exception.BusinessException;
import com.womakerscode.microservicemeetup.servico.agendamento.meetups.model.entity.Registration;
import com.womakerscode.microservicemeetup.servico.agendamento.meetups.repository.RegistrationRepository;
import com.womakerscode.microservicemeetup.servico.agendamento.meetups.service.RegistrationService;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RegistrationServiceImpl implements RegistrationService {

    RegistrationRepository registrationRepository;

    public RegistrationServiceImpl(RegistrationRepository registrationRepository) {
        this.registrationRepository = registrationRepository;
    }

    public Registration save(Registration registration) {
        if (registrationRepository.existsByCode(registration.getCode())){
            throw new BusinessException("Registration already created");
        }

        return registrationRepository.save(registration);
    }

    @Override
    public Optional<Registration> getRegistrationById(Integer id) {
        return registrationRepository.findById(id);
    }

    @Override
    public void delete(Registration registration) {
        if (registration == null || registration.getId() == null){
            throw new IllegalArgumentException("Registration id cannot be null");
        }
        this.registrationRepository.delete(registration);
    }

    @Override
    public Registration update(Registration registration) {
        if (registration == null || registration.getId() == null){
            throw new IllegalArgumentException("Registration id cannot be null");
        }
        return this.registrationRepository.save(registration);
    }

    @Override
    public Page<Registration> find(Registration filter, Pageable pageRequest) {
        Example<Registration> example = Example.of(filter,
                ExampleMatcher
                        .matching()
                        .withIgnoreCase()
                        .withIgnoreNullValues()
                        .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING));
        return registrationRepository.findAll(example,pageRequest);
    }

    @Override
    public Optional<Registration> getRegistrationByCode(String code) {
        return registrationRepository.findByCode(code);
    }
}
