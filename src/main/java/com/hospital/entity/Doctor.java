package com.hospital.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "doctors")
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, length = 100)
    private String specialization;

    @Column(name = "base_consultation_fee", nullable = false, precision = 10, scale = 2)
    private BigDecimal baseConsultationFee = BigDecimal.ZERO;

    @Column(name = "is_available")
    private Boolean isAvailable = true;

    public Doctor() {
    }

    public Doctor(String name, String specialization, BigDecimal baseConsultationFee, Boolean isAvailable) {
        this.name = name;
        this.specialization = specialization;
        this.baseConsultationFee = baseConsultationFee;
        this.isAvailable = isAvailable;
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public BigDecimal getBaseConsultationFee() {
        return baseConsultationFee;
    }

    public void setBaseConsultationFee(BigDecimal baseConsultationFee) {
        this.baseConsultationFee = baseConsultationFee;
    }

    public Boolean getIsAvailable() {
        return isAvailable;
    }

    public void setIsAvailable(Boolean isAvailable) {
        this.isAvailable = isAvailable;
    }
}