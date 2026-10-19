package com.hms.patient.service.service.kafka;


import com.hms.patient.service.model.event.AuditEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class DoctorStatusEventConsumer {
    private final DoctorStatusEventHandler statusEventHandler;


    @KafkaListener(topics = "doctor-status-events", groupId = "patient-service-group")
    public void consumeDoctorStatusChanged(AuditEvent event) {
        statusEventHandler.handle(event);
    }
}
