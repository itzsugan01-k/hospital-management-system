package com.hms.doctor.service.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hms.doctor.service.model.entity.OutboxEvent;
import com.hms.doctor.service.model.request.AuditEvent;
import com.hms.doctor.service.repository.OutBoxRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class DoctorOutboxPublisher {

    private final OutBoxRepository outboxRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final ObjectMapper objectMapper;



    @Scheduled(fixedDelay = 5000)
    public void publishDoctorOutbox() {

        List<OutboxEvent> events = outboxRepository.findTop10ByStatusOrderByCreatedAt("NEW");

        for (OutboxEvent outbox : events) {
            try {
                AuditEvent event = objectMapper.readValue(outbox.getPayload(), AuditEvent.class);

                kafkaTemplate.send(resolveTopic(outbox.getEventType()), outbox.getEventId(), event).get();

                outbox.setStatus("SENT");

            } catch (Exception e) {
                log.error("Failed to publish outbox event {}", outbox.getEventId(), e);
                outbox.setStatus("FAILED");

            }
            outboxRepository.save(outbox);
        }
    }

    private String resolveTopic(String eventType) {
        return switch (eventType) {
            case "DOCTOR_STATUS_CHANGED" -> "doctor-status-events";
            default -> throw new IllegalArgumentException("Unknown event");
        };
    }
}

