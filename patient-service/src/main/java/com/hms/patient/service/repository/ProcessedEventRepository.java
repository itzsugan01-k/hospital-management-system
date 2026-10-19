package com.hms.patient.service.repository;

import com.hms.patient.service.model.entity.ProcessedEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface ProcessedEventRepository extends JpaRepository<ProcessedEvent, String> {
    @Modifying
    @Query("""
            delete from ProcessedEvent p
            where p.processedAt < :cutoff
            """)
    void deleteOlderThan(@Param("cutoff") LocalDateTime cutOff);

    //We store processed Kafka event IDs in a database table and check before processing.
    //This ensures idempotent consumers and safe reprocessing.
}
