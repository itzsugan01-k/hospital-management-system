package com.hms.patient.service.exception;

public class DoctorServiceUnavailableException extends RuntimeException {
    public DoctorServiceUnavailableException(String message) {
        super(message);
    }
}