package com.hms.patient.service.model.entity;
import javax.persistence.*;

import com.hms.patient.service.model.Gender;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "patients", indexes = { @Index(name = "idx_patient_doctor", columnList = "doctorId"),
        @Index(name = "idx_patient_active", columnList = "is_deleted ") } )
@Where(clause = "is_deleted = false")
public class PatientEntity extends BaseAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    @Column(name = "age_years")
    private Integer ageYears;
    @Column(name = "age_months")
    private Integer ageMonths;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Gender gender;
    private String disease;
    // Foreign reference (NO JPA relation)
    @Column(nullable = false)
    private Long doctorId;
    private LocalDate admittedDate;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PatientStatus status;
    @Embedded
    private DoctorSnapshot doctorSnapshot;
    // Business audit fields
    private LocalDateTime doctorChangedAt;
    private String doctorChangedBy;

    @Column(name = "is_deleted")
    private boolean isDeleted = false;

    private LocalDateTime deletedAt;
    private String deletedBy;
}






//actually spring boot 2.8 version only support netflix server , springboot 3 not supported for netflix so downgrade to 2.8  so jakarta.persistance not supported so change javax