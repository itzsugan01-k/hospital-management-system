package com.hms.patient.service.repository;


import com.hms.patient.service.model.entity.PatientEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

public interface PatientRepository extends JpaRepository<PatientEntity, Long> {
    @Modifying
    @Query("""
                update PatientEntity p
                set p.status = 'UNDER_REVIEW'
                where p.doctorId = :doctorId
            """)
    void markDoctorInactive(@Param("doctorId") Long doctorId);

    @Modifying
    @Query("""
            update PatientEntity p
            set p.status = 'ACTIVE'
            where p.doctorId = :doctorId
            and p.status = 'UNDER_REVIEW'
            """)
    void restoreDoctorPatients(Long doctorId);

    @Query(
            value = "SELECT * FROM patients WHERE id = :id",
            nativeQuery = true
    )
    Optional<PatientEntity> findByIncludingDeleted(@Param("id") Long id);

    @Query(
            value = "SELECT * FROM patients WHERE is_deleted = true",
            nativeQuery = true
    )
    List<PatientEntity> findALlDeleted();

    @Modifying
    @Transactional
    @Query(
            value = "DELETE  FROM patients WHERE id = :id",
            nativeQuery = true
    )
    void hardDeleteOn(@Param("id") Long id);
}

