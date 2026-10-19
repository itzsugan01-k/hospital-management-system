package com.hms.patient.service.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hms.patient.service.exception.BusinessException;
import com.hms.patient.service.exception.DoctorNotFoundException;
import com.hms.patient.service.exception.PatientNotFoundException;
import com.hms.patient.service.exception.ServiceUnavailableException;
import com.hms.patient.service.model.entity.DoctorSnapshot;
import com.hms.patient.service.model.entity.OutboxEvent;
import com.hms.patient.service.model.entity.PatientEntity;
import com.hms.patient.service.model.entity.PatientStatus;
import com.hms.patient.service.model.event.AuditEvent;
import com.hms.patient.service.model.request.PatientRequestDto;
import com.hms.patient.service.model.request.PatientUpdateRequestDto;
import com.hms.patient.service.model.response.DoctorDto;
import com.hms.patient.service.model.response.DoctorStatus;
import com.hms.patient.service.model.response.PatientResponseDto;
import com.hms.patient.service.repository.DoctorFeignClient;
import com.hms.patient.service.repository.OutBoxRepository;
import com.hms.patient.service.repository.PatientRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class PatientService {

    @Autowired
    PatientRepository patientRepository;

    @Autowired
    DoctorFeignClient doctorClient;

    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    OutBoxRepository outBoxRepository;



    public Page<PatientResponseDto> getAllPatient(Pageable pageable) {
        //Page<Entity> → Page.map(entity → DTO)
        Page<PatientEntity> entities = patientRepository.findAll(pageable);
        return entities.map(entity -> {
            PatientResponseDto response = new PatientResponseDto();
            response.setId(entity.getId());
            response.setGender(entity.getGender().name());
            response.setAgeYears(entity.getAgeYears());
            response.setAgeMonths(entity.getAgeMonths());
            response.setName(entity.getName());
            response.setDisease(entity.getDisease());
            response.setAdmissionDate(entity.getAdmittedDate());
            response.setDoctorId(entity.getDoctorId());
            if (entity.getDoctorSnapshot() != null) {
                response.setDoctorSummary(new DoctorSnapshot(
                        entity.getDoctorSnapshot().getDoctorName(), entity.getDoctorSnapshot().getSpecialization()));
            }
            return response;
        });

    }

    @Transactional
    @Retry(name = "doctorService")
    @CircuitBreaker(name = "doctorService", fallbackMethod = "createPatientFallback")
    public PatientResponseDto createPatient(PatientRequestDto patient) throws JsonProcessingException {

        // 1️⃣ Explicitly catch NotFound BEFORE Retry/CB can wrap it
        DoctorDto doctor;
        try {
            doctor = doctorClient.getDoctorById(patient.getDoctorId());
            log.info("return from  doctor service ={}", doctor);
        } catch (DoctorNotFoundException ex) {
            // 404 → business error
            throw new DoctorNotFoundException(patient.getDoctorId());

        } // 2️⃣ Business validations
        if (doctor.getStatus() != DoctorStatus.ACTIVE) {
            throw new BusinessException("Doctor is inactive");
        }

        if (!doctor.isAvailable()) {
            throw new BusinessException("Doctor is currently unavailable with id: " + patient.getDoctorId());
        }

        // 3️⃣ Save patient entity
        PatientEntity entity = new PatientEntity();
        entity.setName(patient.getName());
        entity.setAgeYears(patient.getAgeYears());
        entity.setAgeMonths(patient.getAgeMonths());
        entity.setGender(patient.getGender());
        entity.setDisease(patient.getDisease());
        entity.setAdmittedDate(LocalDate.now());
        entity.setStatus(PatientStatus.ADMITTED);
        entity.setDoctorId(doctor.getId());
        entity.setDoctorSnapshot(new DoctorSnapshot(doctor.getName(), doctor.getSpecialization()));

        patientRepository.save(entity);

        //kafka
        AuditEvent event = new AuditEvent(
                UUID.randomUUID().toString(),
                "PATIENT_CREATED",
                "patient-service",
                "PATIENT",
                entity.getId(),
                Map.of(
                        "name", patient.getName(),
                        "ageM", entity.getAgeMonths(),
                        "ageY", entity.getAgeYears()
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

        // ✅ ONLY save to DB
        outBoxRepository.save(outbox);
        return PatientMapper.toMapper(entity, doctor);
    }


    // without database stored to get doctor details
//    @CircuitBreaker(name = "doctorService", fallbackMethod = "doctorFallback")
    public PatientResponseDto getPatientByIdWithoutDatabase(Long id, boolean includeDoctor) {
        PatientEntity entity = patientRepository.findById(id).orElseThrow(() -> new PatientNotFoundException(id));
        DoctorDto doctor = new DoctorDto();
        if (includeDoctor) {
            doctor = doctorClient.getDoctorById(entity.getDoctorId());
        }

        return PatientMapper.toMapper(entity, doctor);
    }

    public void dischargePatient(Long id) {
        PatientEntity entity = patientRepository.findById(id).orElseThrow(() -> new PatientNotFoundException(id));
        entity.setStatus(PatientStatus.DISCHARGED);
        patientRepository.save(entity);
    }

    @Cacheable(value = "patients", key = "#id")
    public PatientResponseDto getPatientById(Long id) {
        System.out.println("Fetching patient from DB");
        PatientEntity entity = patientRepository.findById(id).orElseThrow(() -> new PatientNotFoundException(id));
        return PatientMapper.getPatientResponseDto(entity);
    }

    @Transactional
    @CacheEvict(value = "patients", key = "#id")
    public void deletePatient(@Min(1) Long id) throws JsonProcessingException {
        System.out.println("Fetching patient from DB");
        PatientEntity entity = patientRepository.findById(id).orElseThrow(() -> new PatientNotFoundException(id));
        entity.setDeleted(true);
        entity.setDeletedAt(LocalDateTime.now());
        entity.setDeletedBy("System");
        patientRepository.save(entity);
        //kafka

        AuditEvent event = new AuditEvent(
                UUID.randomUUID().toString(),
                "PATIENT_SOFT_DELETED",
                "patient-service",
                "PATIENT",
                entity.getId(),
                Map.of(
                        "name", entity.getName(),
                        "ageM", entity.getAgeMonths(),
                        "ageY", entity.getAgeYears()
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


    @CacheEvict(value = "patients", key = "#id")
    @Transactional
    @Retry(name = "doctorService")
    @CircuitBreaker(name = "doctorService", fallbackMethod = "changeDoctorFallback")
    public PatientResponseDto updatePatient(@Min(1) Long id, @Valid PatientUpdateRequestDto patient) {
        System.out.println("Fetching patient from DB");
        PatientEntity entity = patientRepository.findById(id).orElseThrow(() -> new PatientNotFoundException(id));
//        entity.setAgeYears(patient.getAgeYears());
//        entity.setAgeMonths(patient.getAgeMonths());
        entity.setDisease(patient.getDisease());
        entity.setGender(patient.getGender());
        entity.setName(patient.getName());
        entity.setDoctorChangedAt(LocalDateTime.now());
        entity.setDoctorChangedBy("System");

        //doctor change logic(optional)
        if (patient.getDoctorId() != null && !patient.getDoctorId().equals(entity.getDoctorId())) {
            DoctorDto doctor = doctorClient.getDoctorById(patient.getDoctorId());
            entity.setDoctorSnapshot(new DoctorSnapshot(doctor.getName(), doctor.getSpecialization()));
            entity.setDoctorId(doctor.getId());
        }
        patientRepository.save(entity);
        return PatientMapper.toResponse(entity);
    }

    public PatientResponseDto createPatientFallback(
            PatientRequestDto request,
            Throwable ex
    ) {


        Throwable cause = ex.getCause() != null ? ex.getCause() : ex;

        if (cause instanceof DoctorNotFoundException) {
            // Throw with actual doctorId from request
            throw new DoctorNotFoundException(request.getDoctorId());
        }

        if (cause instanceof BusinessException) {
            throw (BusinessException) cause;
        }

        // Only technical failures reach here
        throw new ServiceUnavailableException(
                "Doctor service unavailable. Cannot create patient"
        );
    }

    public PatientResponseDto changeDoctorFallback(
            Long patientId, Long newDoctorId, Exception ex) {

        throw new BusinessException("Doctor service unavailable. Try later.");
    }

    @Transactional
    public PatientResponseDto restorePatient(@Min(1) Long id) throws JsonProcessingException {
        PatientEntity entity = patientRepository.findByIncludingDeleted(id).orElseThrow(() -> new PatientNotFoundException(id));
        log.info("enity:{}", entity);
        if (!entity.isDeleted()) {
            throw new BusinessException("Patient is not deleted");
        }
        entity.setDeleted(false);
        entity.setDeletedAt(null);
        entity.setDeletedBy(null);
        entity.setDoctorChangedAt(LocalDateTime.now());
        entity.setDoctorChangedBy("SYSTEM");
        patientRepository.save(entity);
       //kafka
        AuditEvent event = new AuditEvent(
                UUID.randomUUID().toString(),
                "PATIENT_RESTORED",
                "patient-service",
                "PATIENT",
                entity.getId(),
                Map.of(
                        "name", entity.getName(),
                        "ageM", entity.getAgeMonths(),
                        "ageY", entity.getAgeYears()
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

        return PatientMapper.toResponse(entity);
    }

    @Transactional(readOnly = true)
    public List<PatientResponseDto> restoreAllPatient() {
        return patientRepository.findALlDeleted()
                .stream().map(PatientMapper::toResponse).toList();
    }

    @Transactional
    public void permanentlyDeletePatient(Long id) throws JsonProcessingException {
        PatientEntity entity = patientRepository.findByIncludingDeleted(id).orElseThrow(() ->
                new PatientNotFoundException(id));
        patientRepository.hardDeleteOn(id);
        //kafka
        AuditEvent event = new AuditEvent(
                UUID.randomUUID().toString(),
                "PATIENT_HARD_DELETED",
                "patient-service",
                "PATIENT",
                entity.getId(),
                Map.of(
                        "name", entity.getName(),
                        "ageM", entity.getAgeMonths(),
                        "ageY", entity.getAgeYears()
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
}

