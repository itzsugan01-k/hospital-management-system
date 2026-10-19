package com.hms.audit_service.repository;

import com.hms.audit_service.model.entity.ProcessedEvent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProcessedEventRepository
        extends JpaRepository<ProcessedEvent, String> {
}