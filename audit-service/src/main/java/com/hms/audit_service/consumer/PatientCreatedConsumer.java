//package com.hms.audit_service.consumer;
//
//import com.hms.audit_service.model.entity.AuditLog;
//import com.hms.audit_service.model.event.PatientCreatedEvent;
//import com.hms.audit_service.model.entity.ProcessedEvent;
//import com.hms.audit_service.repository.AuditLogRepository;
//import com.hms.audit_service.repository.ProcessedEventRepository;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.kafka.annotation.KafkaListener;
//import org.springframework.stereotype.Service;
//
//import java.time.LocalDateTime;
//
//@Service
//@RequiredArgsConstructor
//@Slf4j
//public class PatientCreatedConsumer {
//    private final AuditLogRepository auditLogRepository;
//    private final ProcessedEventRepository processedEventRepository;
//
//
//    @KafkaListener(topics = "patient-events", groupId = "audit-service-group")
//    public void auditPatientCreated(PatientCreatedEvent event) {
//        log.info("Auditing PatientCreatedEvent: {}", event.getEventId());
//        if (processedEventRepository.existsById(event.getEventId())) {
//            return;
//        }
//        // 2️⃣ Create audit record
//        AuditLog audit = new AuditLog();
//        audit.setEventId(event.getEventId());
//        audit.setEntityType("PATIENT");
//        audit.setEntityId(event.getPatientId());
//        audit.setAction("CREATED");
//        audit.setSourceService(event.getSourceService());
//        audit.setOccurredAt(event.getEventTime());
//
//        // 3️⃣ Save audit
//        auditLogRepository.save(audit);
//        log.info("save PatientCreatedEvent in audit entity: {}", event.getEventId());
//        processedEventRepository.save(new ProcessedEvent(event.getEventId(), LocalDateTime.now()));
//        log.info(" Mark event as processed: {}", event.getEventId());
//    }
//}
