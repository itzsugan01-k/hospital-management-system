package com.hms.doctor.service.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AvailabilityRequest {
    @NotNull(message = "Availability is required")
    private boolean available;
}
