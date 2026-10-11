package com.hospital.repository;

import com.hospital.entity.Admission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AdmissionRepository extends JpaRepository<Admission, Long> {
    List<Admission> findByStatus(Admission.AdmissionStatus status);
}
