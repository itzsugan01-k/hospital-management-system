package com.hms.patient.service.service;

import com.hms.patient.service.model.entity.DoctorSnapshot;
import com.hms.patient.service.model.entity.PatientEntity;
import com.hms.patient.service.model.response.DoctorDto;
import com.hms.patient.service.model.response.PatientResponseDto;

public class PatientMapper {
    public static PatientResponseDto toMapper(PatientEntity entity, DoctorDto doctor) {
        DoctorSnapshot doctorSummary = new DoctorSnapshot();
        Long doctorId=null;
        if (doctor != null) {
            doctorId=doctor.getId();
            doctorSummary.setDoctorName(doctor.getName());
            doctorSummary.setSpecialization(doctor.getSpecialization());
        }
        return new PatientResponseDto(entity.getId(), entity.getName(), entity.getAgeYears(), entity.getAgeMonths(),
                entity.getGender().name(), entity.getDisease(), entity.getAdmittedDate(), doctorId, doctorSummary);
    }

    public static PatientResponseDto toResponse(PatientEntity entity) {

        return new PatientResponseDto(entity.getId(), entity.getName(), entity.getAgeYears(), entity.getAgeMonths(),
                entity.getGender().name(), entity.getDisease(), entity.getAdmittedDate(), entity.getDoctorId(), entity.getDoctorSnapshot());
    }

    public static PatientResponseDto getPatientResponseDto(PatientEntity entity) {
        PatientResponseDto response = new PatientResponseDto();
        response.setId(entity.getId());
        response.setGender(entity.getGender().name());
        response.setAgeYears(entity.getAgeYears());
        response.setAgeMonths(entity.getAgeMonths());
        response.setName(entity.getName());
        response.setDisease(entity.getDisease());
        response.setAdmissionDate(entity.getAdmittedDate());
        response.setDoctorSummary(new DoctorSnapshot(
                entity.getDoctorSnapshot().getDoctorName(), entity.getDoctorSnapshot().getSpecialization()));
        return response;
    }

}
