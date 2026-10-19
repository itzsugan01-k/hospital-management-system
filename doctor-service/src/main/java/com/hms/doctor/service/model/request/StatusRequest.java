package com.hms.doctor.service.model.request;

import com.hms.doctor.service.model.entity.DoctorStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatusRequest {
    @NotNull(message = "Status is required")
    private DoctorStatus status;
}
