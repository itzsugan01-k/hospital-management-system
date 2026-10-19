package com.hms.doctor.service.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuditEvent {

    private String eventId;          // UUID
    private String eventType;        // DOCTOR_STATUS_CHANGED / PATIENT_CREATED
    private String sourceService;    // doctor-service / patient-service
    private String entityType;       // DOCTOR / PATIENT
    private Long entityId;         // doctorId / patientId

    private Object payload;          // event-specific data (Map or DTO)

    private LocalDateTime eventTime;
}

