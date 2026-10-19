package com.hms.patient.service.model.request;

import com.hms.patient.service.model.Gender;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PatientUpdateRequestDto {

    @NotBlank(message = "name is required")
    private String name;

    @Min(message = "age must be greater than 0", value = 1)
    private Integer age;

    @NotBlank
    private String disease;
    private Gender gender;

    private Long doctorId; // optional (change doctor)
}