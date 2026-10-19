package com.hms.audit_service.model.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PatientLifecycleEvent {
    private String eventId;
    private Long patientId;
    private String action; // SOFT_DELETE, HARD_DELETE, RESTORE
    private LocalDateTime eventTime;
    private String sourceService;
}