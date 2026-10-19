package com.hms.doctor.service.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "doctors",
        indexes = {
                @Index(name = "idx_doctor_status", columnList = "status"),
                @Index(name = "idx_doctor_available", columnList = "available")
        }
)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class DoctorEntity extends BaseAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    private String specialization;
    private Integer experience;
    private String phone;
    // Temporary state (shift-based)
    private boolean available;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DoctorStatus status;

    // Business-specific audit fields
    private LocalDateTime specializationChangedAt;
    private String specializationChangedBy;

    private LocalDateTime statusChangedAt;
    private String statusChangedBy;

    private boolean isDeleted = false;
    private LocalDateTime deletedAt;
    private String deletedBy;
}

