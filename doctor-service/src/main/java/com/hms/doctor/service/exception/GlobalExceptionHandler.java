package com.hms.doctor.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {
   @ExceptionHandler(DoctorNotFoundException.class)
    public ResponseEntity<ApiError> handlePatientNotFound(DoctorNotFoundException px){
       return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiError(404,px.getMessage()));
   }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiError> handleBusinessException(BusinessException px){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiError(400,px.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGenericException(Exception px){
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiError(500,"INTERNAL_SERVER_ERROR"));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidationException(
            MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );


        return ResponseEntity.badRequest().body(new ApiError(400,"VALIDATION_ERROR",errors));
    }
}
