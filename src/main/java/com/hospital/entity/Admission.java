package com.hospital.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "admissions")
public class Admission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id")
    private Patient patient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id")
    private Doctor doctor;

    private BigDecimal registrationFee = new BigDecimal("1500.00");

    @Enumerated(EnumType.STRING)
    private Severity injurySeverity;

    @Enumerated(EnumType.STRING)
    private AdmissionStatus status = AdmissionStatus.ACTIVE;

    private LocalDateTime admittedAt = LocalDateTime.now();
    private LocalDateTime dischargedAt;

    @OneToMany(mappedBy = "admission", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BillingItem> billingItems = new ArrayList<>();

    public enum Severity { MINOR, MODERATE, MAJOR }
    public enum AdmissionStatus { ACTIVE, DISCHARGED }

    // Helper methods for bidirectional mapping
    public void addBillingItem(BillingItem item) {
        billingItems.add(item);
        item.setAdmission(this);
    }

    public void removeBillingItem(BillingItem item) {
        billingItems.remove(item);
        item.setAdmission(null);
    }

    // Standard Getters/Setters
    public Long getId() { return id; }
    public Patient getPatient() { return patient; }
    public void setPatient(Patient patient) { this.patient = patient; }
    public Doctor getDoctor() { return doctor; }
    public void setDoctor(Doctor doctor) { this.doctor = doctor; }
    public BigDecimal getRegistrationFee() { return registrationFee; }
    public Severity getInjurySeverity() { return injurySeverity; }
    public void setInjurySeverity(Severity injurySeverity) { this.injurySeverity = injurySeverity; }
    public AdmissionStatus getStatus() { return status; }
    public void setStatus(AdmissionStatus status) { this.status = status; }
    public LocalDateTime getDischargedAt() { return dischargedAt; }
    public void setDischargedAt(LocalDateTime dischargedAt) { this.dischargedAt = dischargedAt; }
    public List<BillingItem> getBillingItems() { return billingItems; }
}