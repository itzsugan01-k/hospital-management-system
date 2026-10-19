package com.hms.doctor.service.model.response;

import com.hms.doctor.service.model.entity.DoctorStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DoctorResponseDto {

    private Long id;
    private String name;
    private String specialization;
    private Integer experience;
    private Boolean available;
    private DoctorStatus status;
}