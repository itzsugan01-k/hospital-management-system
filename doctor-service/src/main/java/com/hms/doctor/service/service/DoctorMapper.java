package com.hms.doctor.service.service;

import com.hms.doctor.service.model.entity.DoctorEntity;
import com.hms.doctor.service.model.response.DoctorResponseDto;

public class DoctorMapper {
    public static DoctorResponseDto toResponse(DoctorEntity entity) {
        DoctorResponseDto responseDto = new DoctorResponseDto();
        responseDto.setId(entity.getId());
        responseDto.setName(entity.getName());
        responseDto.setSpecialization(entity.getSpecialization());
        responseDto.setExperience(entity.getExperience());
        responseDto.setAvailable(entity.isAvailable());
        responseDto.setStatus(entity.getStatus());
        return responseDto;
    }
}
