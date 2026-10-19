//package com.hms.audit_service.consumer;
//
//import com.hms.audit_service.model.entity.AuditLog;
//import com.hms.audit_service.model.event.DoctorStatusChangedEvent;
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
//public class DoctorStatusChangeConsumer {
//    private final AuditLogRepository auditLogRepository;
//    private final ProcessedEventRepository processedEventRepository;
//
//
//    @KafkaListener(topics = "doctor-status-events", groupId = "audit-service-group")
//    public void auditPatientCreated(DoctorStatusChangedEvent event) {
//        log.info("Auditing DoctorStatusChangedEvent: {}", event.getEventId());
//        if (processedEventRepository.existsById(event.getEventId())) {
//            return;
//        }
//
//        AuditLog audit = new AuditLog();
//        audit.setEventId(event.getEventId());
//        audit.setEntityType("DOCTOR");
//        audit.setEntityId(event.getDoctorId());
//        audit.setAction("STATUS_CHANGE");
//        audit.setOldValue(event.getOldStatus());
//        audit.setNewValue(event.getNewStatus());
//        audit.setSourceService(event.getSourceService());
//        audit.setOccurredAt(event.getEventTime());
//        auditLogRepository.save(audit);
//
//        processedEventRepository.save(
//                new ProcessedEvent(event.getEventId(), LocalDateTime.now())
//        );
//
//        log.info("Audited DoctorStatusChangedEvent {}", event.getEventId());
//    }
//}
