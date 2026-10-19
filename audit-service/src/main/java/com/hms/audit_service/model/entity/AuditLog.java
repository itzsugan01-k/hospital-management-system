package com.hms.audit_service.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "audit_logs",
        indexes = {
                @Index(name = "idx_entity", columnList = "entityType, entityId"),
                @Index(name = "idx_event", columnList = "eventId")
        })
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String eventId;
    private String eventType;
    private String entityType;   // PATIENT, DOCTOR
    private Long entityId;

    private String payload;
    private String sourceService;

    private LocalDateTime occurredAt;


}
