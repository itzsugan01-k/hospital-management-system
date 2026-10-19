package com.hms.audit_service.model.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PatientCreatedEvent {
    private String eventId;
    private Long patientId;
    private String patientName;   // optional
    private LocalDateTime eventTime;
    private String sourceService;
}