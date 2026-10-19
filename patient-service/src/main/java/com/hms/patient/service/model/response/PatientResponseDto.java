package com.hms.patient.service.model.response;

import com.hms.patient.service.model.entity.DoctorSnapshot;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PatientResponseDto {

    private Long id;
    private String name;
    private Integer ageYears;
    private Integer ageMonths;
    private String gender;
    private String disease;
    private LocalDate admissionDate;
    private Long doctorId;

    private DoctorSnapshot doctorSummary;
}