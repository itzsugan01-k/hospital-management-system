package com.hms.patient.service.repository;

import com.hms.patient.service.config.FeignConfig;
import com.hms.patient.service.model.response.DoctorDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "DOCTOR-SERVICE", configuration = FeignConfig.class)
public interface DoctorFeignClient {
    @GetMapping("/doctor/v1/doctors/{id}")
    public DoctorDto getDoctorById(@PathVariable Long id);
}
