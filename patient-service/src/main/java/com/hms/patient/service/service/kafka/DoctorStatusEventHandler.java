package com.hms.patient.service.service.kafka;

import com.hms.patient.service.model.entity.ProcessedEvent;
import com.hms.patient.service.model.event.AuditEvent;
import com.hms.patient.service.repository.PatientRepository;
import com.hms.patient.service.repository.ProcessedEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class DoctorStatusEventHandler {

    private final ProcessedEventRepository processedEventRepository;
    private final PatientRepository patientRepository;

    @Transactional
    public void handle(AuditEvent event) {

        // 1. Idempotency check
        if (processedEventRepository.existsById(event.getEventId())) {
            return;
        }

        // 2. Business logic
        if ("DOCTOR_STATUS_CHANGED".equals(event.getEventType())) {
            Map<String, Object> payload = (Map<String, Object>) event.getPayload();

            String newStatus = (String) payload.get("newStatus");
            // 2️⃣ Business logic (may fail → retry)
            if ("INACTIVE".equals(newStatus)) {
                handleDoctorInactive(event.getEntityId());
            } else if ("ACTIVE".equals(newStatus)) {
                handleDoctorActive(event.getEntityId());
            }
            // 3️⃣ Mark event as processed (ONLY after success)
            processedEventRepository.save(new ProcessedEvent(event.getEventId(), LocalDateTime.now()));
        }
    }

        private void handleDoctorActive(Long doctorId) {
            patientRepository.restoreDoctorPatients(doctorId);
        }

        private void handleDoctorInactive(Long doctorId) {
            patientRepository.markDoctorInactive(doctorId);
        }
}