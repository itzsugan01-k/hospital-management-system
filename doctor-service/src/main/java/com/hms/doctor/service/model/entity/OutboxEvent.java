package com.hms.doctor.service.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "outbox_event")
public class OutboxEvent {

    @Id
    @Column(name = "event_id", length = 100, nullable = false)
    private String eventId;

    private String aggregateType;   // PATIENT
    private Long aggregateId;        // patientId

    private String eventType;        // PATIENT_CREATED, PATIENT_LIFECYCLE
    private String payload;          // JSON

    private String status;           // NEW, SENT, FAILED
    private LocalDateTime createdAt;

}


