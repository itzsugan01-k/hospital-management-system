package com.hms.doctor.service.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DoctorRequestDto {
    @NotBlank(message = "Doctor name is required")
    @Size(min = 3, max = 50, message = "Doctor name must be between 3 and 50 characters")
    private String name;
    @NotBlank(message = "Specialization is required")
    private String specialization;
    @NotNull(message = "Experience is required")
    @Min(value = 0, message = "Experience cannot be negative")
    @Max(value = 60, message = "Experience cannot be more than 60 years")
    private Integer experience;
    @Pattern(
            regexp = "^[6-9]\\d{9}$",
            message = "Phone number must be a valid 10-digit Indian number"
    )
    private String phone;
}