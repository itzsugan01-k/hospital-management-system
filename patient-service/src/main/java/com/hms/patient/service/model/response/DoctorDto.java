package com.hms.patient.service.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DoctorDto {
    private Long id;

    public DoctorDto(Long id, String name, String specialization) {
        this.id = id;
        this.name = name;
        this.specialization = specialization;
    }

    private String name;
    private String specialization;
    private boolean available;
    private DoctorStatus status;
}
