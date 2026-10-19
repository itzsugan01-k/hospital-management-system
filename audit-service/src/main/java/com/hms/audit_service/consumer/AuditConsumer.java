package com.hms.audit_service.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hms.audit_service.model.entity.AuditLog;
import com.hms.audit_service.model.entity.ProcessedEvent;
import com.hms.audit_service.model.event.AuditEvent;
import com.hms.audit_service.repository.AuditLogRepository;
import com.hms.audit_service.repository.ProcessedEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuditConsumer {
    private final AuditLogRepository auditLogRepository;
    private final ProcessedEventRepository processedEventRepository;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = {"doctor-status-events", "patient-events", "patient-lifecycle-events"},
            groupId = "audit-service-group")
    public void audit(AuditEvent event) throws JsonProcessingException {
        log.info("Auditing events: {}", event.getEventId());
        if (processedEventRepository.existsById(event.getEventId())) {
            return;
        }
        AuditLog auditLog = new AuditLog();
        auditLog.setEntityId(event.getEntityId());
        auditLog.setEventId(event.getEventId());
        auditLog.setEntityType(event.getEntityType());
        auditLog.setOccurredAt(LocalDateTime.now());
        auditLog.setSourceService(event.getSourceService());
        auditLog.setEventType(event.getEventType());
        auditLog.setOccurredAt(event.getEventTime());
        auditLog.setPayload(objectMapper.writeValueAsString(event.getPayload()));
        auditLogRepository.save(auditLog);

        processedEventRepository.save(new ProcessedEvent(event.getEventId(), LocalDateTime.now()));
    }
}
