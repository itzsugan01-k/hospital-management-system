package com.hms.patient.service.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.hms.patient.service.model.request.PatientRequestDto;
import com.hms.patient.service.model.response.PatientResponseDto;
import com.hms.patient.service.model.request.PatientUpdateRequestDto;
import com.hms.patient.service.service.PatientService;
import lombok.extern.slf4j.Slf4j;
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
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/patients")
@Validated
public class PatientsController {
    @Autowired
    PatientService patientService;

    @GetMapping()
    public Page<PatientResponseDto> getAllPatient(@PageableDefault(size = 10, sort = "id") Pageable pageable) {
        return patientService.getAllPatient(pageable);
    }

    @PostMapping
    public ResponseEntity<PatientResponseDto> createPatient(@Valid @RequestBody PatientRequestDto patient) throws JsonProcessingException {
        log.info("create patient :{}", patient);
        return ResponseEntity.ok(patientService.createPatient(patient));

    }

    // to get doctor details driectly to call to doctor service
    @GetMapping("/{id}/includeDoctor")
    public ResponseEntity<PatientResponseDto> getPatientByIdWithoutDatabase(@PathVariable @Min(1) Long id,
                                                                            @RequestParam(defaultValue = "false") boolean includeDoctor) {
        return ResponseEntity.ok(patientService.getPatientByIdWithoutDatabase(id, includeDoctor));
    }

    // to save  doctor datils in database
    @GetMapping("/{id}")
    public ResponseEntity<PatientResponseDto> getPatientById(@PathVariable @Min(1) Long id) {
        return ResponseEntity.ok(patientService.getPatientById(id));
    }

    @PutMapping("/{id}/update")
    public ResponseEntity<PatientResponseDto> updatePatient(@PathVariable @Min(1) Long id, @Valid @RequestBody PatientUpdateRequestDto patient) {
        return ResponseEntity.ok(patientService.updatePatient(id, patient));
    }

    @PutMapping("/{id}")
    public String dischargePatient(@PathVariable @Min(1) Long id) {
        patientService.dischargePatient(id);
        return "Patient discharge successfully";
    }

    @PutMapping("/{id}/restore")
    public ResponseEntity<PatientResponseDto> restorePatient(@PathVariable @Min(1) Long id) throws JsonProcessingException {
        return ResponseEntity.ok(patientService.restorePatient(id));

    }

    @DeleteMapping("/{id}")
    public String deletePatient(@PathVariable @Min(1) Long id) throws JsonProcessingException {
        patientService.deletePatient(id);
        return "Patient deleted successfully";
    }

    @DeleteMapping("/deleted")
    public ResponseEntity<List<PatientResponseDto>> restoreAllPatient() {
        return ResponseEntity.ok(patientService.restoreAllPatient());

    }
    @DeleteMapping("/permanent/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void permanentlyDelete(@PathVariable Long id) throws JsonProcessingException {
        patientService.permanentlyDeletePatient(id);
    }
}



