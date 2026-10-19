//package com.hms.audit_service.consumer;
//
//import com.hms.audit_service.model.entity.AuditLog;
//import com.hms.audit_service.model.event.PatientLifecycleEvent;
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
//@Slf4j
//@RequiredArgsConstructor
//public class PatientLifeCycleConsumer {
//    private final AuditLogRepository auditLogRepository;
//    private final ProcessedEventRepository processedEventRepository;
//
//    @KafkaListener(
//            topics = "patient-lifecycle-events",
//            groupId = "audit-service-group"
//    )
//    public void auditPatientLifecycle(PatientLifecycleEvent event) {
//
//        // 1️⃣ Idempotency
//        if (processedEventRepository.existsById(event.getEventId())) {
//            return;
//        }
//        AuditLog audit = new AuditLog();
//        audit.setEventId(event.getEventId());
//        audit.setEntityType("PATIENT");
//        audit.setEntityId(event.getPatientId());
//        audit.setAction(event.getAction());
//        audit.setSourceService(event.getSourceService());
//        audit.setOccurredAt(event.getEventTime());
//
//
//        auditLogRepository.save(audit);
//
//    // 3️⃣ Mark processed
//        processedEventRepository.save(
//                new ProcessedEvent(event.getEventId(), LocalDateTime.now())
//            );
//
//        log.info("Audited PatientLifecycleEvent {}", event.getEventId());
//}
//}
