package com.hms.patient.service.repository;


import com.hms.patient.service.model.entity.OutboxEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OutBoxRepository extends JpaRepository<OutboxEvent,Long> {
    List<OutboxEvent> findTop10ByStatusOrderByCreatedAt(String status);
}
