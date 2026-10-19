package com.hms.doctor.service.exception;

public class BusinessException extends RuntimeException{
    public BusinessException(String message){
        super(message);
    }
    public BusinessException(String message, Throwable ex){
        super(message,ex);
    }
}
