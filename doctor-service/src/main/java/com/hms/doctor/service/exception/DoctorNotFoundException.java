package com.hms.doctor.service.exception;

public class DoctorNotFoundException extends BusinessException{
    public DoctorNotFoundException(Long id){
        super("Doctor not found with id:" +id);
    }
}
