package com.hms.patient.service.service;

import com.hms.patient.service.repository.ProcessedEventRepository;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

@Component
@EnableScheduling
public class ProcessedEventCleanUpJob {
    private final ProcessedEventRepository eventRepository;

    public ProcessedEventCleanUpJob(ProcessedEventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @Scheduled(cron = "0 0 2 * * ?")
    @Transactional
    public void cleanupOldEvents() {
        eventRepository.deleteOlderThan(
                LocalDateTime.now().minusDays(30)
        );
    }
}
