package com.hms.patient.service.model;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum Gender {
    MALE,FEMALE,OTHER;

    @JsonCreator
    public static Gender from(Object value) {
        if (value == null) return null;
        return Gender.valueOf(value.toString().trim().toUpperCase());
    }
}


