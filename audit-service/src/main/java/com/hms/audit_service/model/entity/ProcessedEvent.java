package com.hms.audit_service.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "processed_events")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProcessedEvent {

    @Id
    @Column(length = 100)
    private String eventId;

    private LocalDateTime processedAt;
}