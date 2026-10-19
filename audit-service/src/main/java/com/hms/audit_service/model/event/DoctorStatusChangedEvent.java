package com.hms.audit_service.model.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DoctorStatusChangedEvent {

    private String eventId;
    private Long doctorId;

    private String oldStatus;   // ACTIVE
    private String newStatus;   // INACTIVE

    private LocalDateTime eventTime;
    private String sourceService = "doctor-service";
}