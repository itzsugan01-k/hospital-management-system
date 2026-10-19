package com.hms.patient.service.config;

import com.hms.patient.service.exception.DoctorNotFoundException;
import com.hms.patient.service.exception.DoctorServiceUnavailableException;
import feign.Response;
import feign.codec.ErrorDecoder;

public class FeignErrorCoder implements ErrorDecoder {
    @Override
    public Exception decode(String s, Response response) {
        if(response.status() == 404){
            return new DoctorNotFoundException((Long) null);
        }
        if(response.status() == 503){
            return new DoctorServiceUnavailableException("Doctor service unavailable");
        }
        return new RuntimeException( "Unexpected error from Doctor service. Status: " + response.status());
    }
}



