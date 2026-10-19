package com.hms.patient.service.model.request;

import com.hms.patient.service.model.Gender;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PatientRequestDto {
    @NotBlank(message = "Patient name is required")
    private String name;
    @NotNull(message = "Gender is required")
    private Gender gender;
    @NotBlank(message = "disease is required")
    private String disease;
    @NotNull(message = "doctor id is required")
    private Long doctorId;
    @Min(value = 0, message = "Age in years cannot be negative")
    @Max(value = 120, message = "Age in years cannot exceed 120")
    private Integer ageYears;

    @Min(value = 0, message = "Age in months cannot be negative")
    @Max(value = 11, message = "Age in months cannot exceed 11")
    private Integer ageMonths;

    @AssertTrue(message = "If ageYears is 0, ageMonths must be between 0 and 11")
    private boolean isAgeValid() {
        if (ageYears == null || ageMonths == null) return true;
        if (ageYears == 0) return ageMonths >= 0 && ageMonths <= 11;
        return true;
    }
}