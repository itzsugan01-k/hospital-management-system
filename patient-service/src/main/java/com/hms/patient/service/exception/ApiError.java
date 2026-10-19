package com.hms.patient.service.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiError {

    private int status;
    private String message;
    private Map<String, String> error;


    public ApiError(int status, String message) {
        this.status = status;
        this.message = message;
    }


}


