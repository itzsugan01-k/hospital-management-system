package com.hms.doctor.service.repository;

import com.hms.doctor.service.model.entity.OutboxEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OutBoxRepository extends JpaRepository<OutboxEvent,Long> {
    List<OutboxEvent> findTop10ByStatusOrderByCreatedAt(String status);
}
