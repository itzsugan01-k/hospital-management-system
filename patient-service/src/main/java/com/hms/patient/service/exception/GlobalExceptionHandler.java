package com.hms.patient.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {
   @ExceptionHandler(PatientNotFoundException.class)
    public ResponseEntity<ApiError> handlePatientNotFound(PatientNotFoundException px ){
       return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiError(404,px.getMessage()));
   }
    @ExceptionHandler(DoctorNotFoundException.class)
    public ResponseEntity<ApiError> handleDoctorNotFound(DoctorNotFoundException px ){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiError(404,px.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGenericException(Exception ex ){
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiError(500,"INTERNAL_SERVER_ERROR"));
    }


    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiError> handleBusinessException(
            BusinessException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiError(400,ex.getMessage()));
    }



    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidationException(
            MethodArgumentNotValidException ex){

        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );


        return ResponseEntity.badRequest().body(new ApiError(400, "VALIDATION_ERROR",errors));
    }


    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiError> handleBadJson(HttpMessageNotReadableException ex) {
        return ResponseEntity.badRequest()
                .body(new ApiError(400,"Invalid request format. Please check JSON body"));
    }


    @ExceptionHandler(ServiceUnavailableException.class)
    public ResponseEntity<Map<String,Object>> handleServiceUnavailable(ServiceUnavailableException ex){
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(Map.of("status",503,"message",ex.getMessage(),"error",null));
    }

}
