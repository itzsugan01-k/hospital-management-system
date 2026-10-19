package com.hms.doctor.service.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.hms.doctor.service.model.request.AvailabilityRequest;
import com.hms.doctor.service.model.request.DoctorRequestDto;
import com.hms.doctor.service.model.request.StatusRequest;
import com.hms.doctor.service.model.response.DoctorResponseDto;
import com.hms.doctor.service.service.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;

@RestController
@RequestMapping("/doctors")
@Validated
public class DoctorController {
    @Autowired
    DoctorService doctorService;

    @GetMapping()
    public Page<DoctorResponseDto> getAllDoctor(@PageableDefault(size = 10, sort = "id") Pageable pageable) {
        return doctorService.getAllDoctors(pageable);
    }

    @PostMapping("/add")
    public ResponseEntity<DoctorResponseDto> addDoctor(@Valid @RequestBody DoctorRequestDto doctor) {
        DoctorResponseDto responseDto = doctorService.addDoctor(doctor);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DoctorResponseDto> getDoctorById(@PathVariable @Min(1) Long id) {
        DoctorResponseDto responseDto = doctorService.getDoctorById(id);
        return ResponseEntity.ok(responseDto);

    }

    @PutMapping("/{id}/availability")
    public ResponseEntity<Void> updateAvailability(@PathVariable @Min(1) Long id, @Valid @RequestBody AvailabilityRequest doctor) {
        doctorService.updateAvailability(id, doctor);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Void> updateStatus(@PathVariable @Min(1) Long id, @Valid @RequestBody StatusRequest doctor) throws JsonProcessingException {
        doctorService.updateStatus(id, doctor);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/specialization")
    public ResponseEntity<DoctorResponseDto> changeSpecialization(@PathVariable @Min(1) Long id,  @Valid @RequestParam String specialization) {
        return ResponseEntity.ok(doctorService.changeSpecialization(id, specialization));
    }

    @PutMapping("/doctor/{id}")
    public ResponseEntity<DoctorResponseDto> updateDoctor(@PathVariable @Min(1) Long id, @Valid @RequestBody DoctorRequestDto request) {
        return ResponseEntity.ok(doctorService.updateDoctor(id, request));
    }

    @PutMapping("/{id}/restore")
    public ResponseEntity<DoctorResponseDto> restoreDoctor(@PathVariable @Min(1) Long id) {
        return ResponseEntity.ok(doctorService.restoreDoctor(id));

    }

    @DeleteMapping("/{id}")
    public String deletePatient(@PathVariable @Min(1) Long id) {
        doctorService.deletePatient(id);
        return "Patient deleted successfully";
    }
    @DeleteMapping
    public ResponseEntity<Void> deleteAll(){
        doctorService.deleteAll();
        return ResponseEntity.ok().build();
    }
}
