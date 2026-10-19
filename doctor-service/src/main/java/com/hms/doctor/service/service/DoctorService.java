package com.hms.doctor.service.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hms.doctor.service.exception.BusinessException;
import com.hms.doctor.service.exception.DoctorNotFoundException;
import com.hms.doctor.service.model.entity.DoctorEntity;
import com.hms.doctor.service.model.entity.DoctorStatus;
import com.hms.doctor.service.model.entity.OutboxEvent;
import com.hms.doctor.service.model.request.*;
import com.hms.doctor.service.model.response.DoctorResponseDto;
import com.hms.doctor.service.repository.DoctorRepository;
import com.hms.doctor.service.repository.OutBoxRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Service
public class DoctorService {
    @Autowired
    DoctorRepository repository;
    @Autowired
    DoctorOutboxPublisher doctorEventPublisher;
    @Autowired
    OutBoxRepository outBoxRepository;
    @Autowired
    ObjectMapper objectMapper;

    @Transactional
    public DoctorResponseDto addDoctor(DoctorRequestDto doctor) {

        DoctorEntity entity = new DoctorEntity();
        entity.setAvailable(Boolean.TRUE);
        entity.setName(doctor.getName());
        entity.setExperience(doctor.getExperience());
        entity.setSpecialization(doctor.getSpecialization());
        entity.setPhone(doctor.getPhone());
        entity.setStatus(DoctorStatus.ACTIVE);
        repository.save(entity);
        return DoctorMapper.toResponse(entity);
    }

    @Transactional
    public Page<DoctorResponseDto> getAllDoctors(Pageable pageable) {

        Page<DoctorEntity> entities = repository.findAll(pageable);
        return entities.map(entity -> {
            DoctorResponseDto doctorResponse = new DoctorResponseDto();
            doctorResponse.setId(entity.getId());
            doctorResponse.setAvailable(entity.isAvailable());
            doctorResponse.setName(entity.getName());
            doctorResponse.setExperience(entity.getExperience());
            doctorResponse.setSpecialization(entity.getSpecialization());
            doctorResponse.setStatus(entity.getStatus());
            return doctorResponse;
        });
    }

    @Transactional
    @CacheEvict(value = "doctors", key = "#id")
    public void updateAvailability(Long id, AvailabilityRequest request) {
        DoctorEntity entity = repository.findById(id).orElseThrow(() -> new DoctorNotFoundException(id));
        if (entity.getStatus() != DoctorStatus.ACTIVE) {
            throw new BusinessException("Inactive doctor cannot change availability");
        }
        entity.setAvailable(request.isAvailable());
        repository.save(entity);
    }

    @Transactional
    @Cacheable(value = "doctors", key = "#id")
    public DoctorResponseDto getDoctorById(Long id) {
        System.out.println("Calling DB for doctor id = " + id); //for 2nd time database not called because of cache
        DoctorEntity entity = repository.findById(id).orElseThrow(() -> new DoctorNotFoundException(id));
        return DoctorMapper.toResponse(entity);

    }

    //update evict cache
    @Transactional
    @CacheEvict(value = "doctors", key = "#id")
    public DoctorResponseDto updateDoctor(@Min(1) Long id, @Valid DoctorRequestDto request) {
        DoctorEntity entity = repository.findById(id)
                .orElseThrow(() -> new DoctorNotFoundException(id));

        entity.setName(request.getName());
        entity.setSpecialization(request.getSpecialization());
        entity.setExperience(request.getExperience());
        entity.setSpecialization(request.getSpecialization());
        entity.setPhone(request.getPhone());
        repository.save(entity);
        return DoctorMapper.toResponse(entity);
    }

    @Transactional
    @CacheEvict(value = "doctors", key = "#id")
    public DoctorResponseDto changeSpecialization(@Min(1) Long id, String specialization) {
        DoctorEntity entity = repository.findById(id)
                .orElseThrow(() -> new DoctorNotFoundException(id));
        entity.setSpecialization(specialization);
        entity.setSpecializationChangedAt(LocalDateTime.now());
        entity.setSpecializationChangedBy("SYSTEM");
        repository.save(entity);
        return DoctorMapper.toResponse(entity);

    }

    @Transactional
    @CacheEvict(value = "doctors", key = "#id")
    public void deletePatient(@Min(1) Long id) {
        DoctorEntity entity = repository.findById(id)
                .orElseThrow(() -> new DoctorNotFoundException(id));
        entity.setDeleted(true);
        entity.setDeletedAt(LocalDateTime.now());
        entity.setDeletedBy("SYSTEM");
        repository.save(entity);
    }

    @Transactional
    @CacheEvict(value = "doctors", key = "#id")
    public DoctorResponseDto restoreDoctor(@Min(1) Long id) {
        DoctorEntity entity = repository.findById(id)
                .orElseThrow(() -> new DoctorNotFoundException(id));
        if (!entity.isDeleted()) {
            entity.setDeleted(false);
            entity.setDeletedAt(null);
            entity.setDeletedBy(null);
        }
        repository.save(entity);
        return DoctorMapper.toResponse(entity);
    }

    @Transactional
    @CacheEvict(value = "doctors", key = "#id")
    public void updateStatus(@Min(1) Long id, @Valid StatusRequest doctor) throws JsonProcessingException {
        DoctorEntity entity = repository.findById(id)
                .orElseThrow(() -> new DoctorNotFoundException(id));
        if (entity.getStatus() == doctor.getStatus()) {
            return;
        }
        String oldStatus=entity.getStatus().name();
        if (doctor.getStatus() == DoctorStatus.INACTIVE) {
            entity.setAvailable(false);  // force unavailable
        }
        entity.setStatus(doctor.getStatus());
        entity.setStatusChangedAt(LocalDateTime.now());
        entity.setStatusChangedBy("SYSTEM");
        repository.save(entity);


        AuditEvent event = new AuditEvent(
                UUID.randomUUID().toString(),
                "DOCTOR_STATUS_CHANGED",
                "doctor-service",
                "DOCTOR",
                id,
                Map.of(
                        "oldStatus", oldStatus,
                        "newStatus", doctor.getStatus()
                ),
                LocalDateTime.now()
        );

        OutboxEvent outbox = new OutboxEvent();
        outbox.setEventId(event.getEventId());
        outbox.setAggregateType(event.getEntityType());
        outbox.setAggregateId(entity.getId());
        outbox.setEventType(event.getEventType());
        outbox.setPayload(objectMapper.writeValueAsString(event));
        outbox.setStatus("NEW");
        outbox.setCreatedAt(LocalDateTime.now());
        outBoxRepository.save(outbox);

    }

    public void deleteAll() {
        repository.deleteAll();
    }
}
