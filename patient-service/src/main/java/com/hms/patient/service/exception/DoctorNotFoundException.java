package com.hms.patient.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class DoctorNotFoundException extends BusinessException{
    public DoctorNotFoundException(Long id){
        super("Doctor not found with id:" +id);
    }
    public DoctorNotFoundException(String message){
        super("Doctor not found for given id");
    }
}
