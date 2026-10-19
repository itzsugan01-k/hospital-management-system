package com.hms.patient.service.exception;

public class BusinessException extends RuntimeException{
    public BusinessException(String message){
        super(message);
    }
    public BusinessException(String message,Throwable ex){
        super(message,ex);
    }
}
