package com.hms.doctor.service.repository;

import com.hms.doctor.service.model.entity.DoctorEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DoctorRepository extends JpaRepository<DoctorEntity,Long> {
    List<DoctorEntity>findBySpecialization(String specialization);
    List<DoctorEntity>findByAvailableTrue();
}
