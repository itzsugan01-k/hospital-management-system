package com.hms.patient.service.service.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hms.patient.service.model.entity.OutboxEvent;
import com.hms.patient.service.model.event.AuditEvent;
import com.hms.patient.service.repository.OutBoxRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class OutBoxPublisher {
    private final KafkaTemplate<String,Object> kafkaTemplate;
    private final OutBoxRepository outBoxRepository;
    private final ObjectMapper objectMapper;

    @Scheduled(fixedDelay = 5000)
    @Transactional
    public void publishOutBoxEvent(){
        List<OutboxEvent> pendingEvents=outBoxRepository.findTop10ByStatusOrderByCreatedAt("NEW");
        for(OutboxEvent outBox: pendingEvents){
            try{
                AuditEvent event=objectMapper.readValue(outBox.getPayload(),AuditEvent.class);
                kafkaTemplate.send(resolveTopic(outBox.getEventType()),outBox.getEventId(),event);
                outBox.setStatus("SEND");

            }catch (Exception e){
                outBox.setStatus("FAILED");
               log.info("Failed to publish outbox {}",e.getMessage());
            }
            outBoxRepository.save(outBox);
        }


    }
    private String resolveTopic(String eventType) {
        return switch (eventType) {
            case "PATIENT_CREATED" -> "patient-events";
            case "PATIENT_LIFECYCLE" -> "patient-lifecycle-events";
            default -> throw new IllegalArgumentException("Unknown event");
        };
    }

}
