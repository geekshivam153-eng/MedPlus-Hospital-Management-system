package com.hospital.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "patients")
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String uniqueUhid;

    private String fullName;
    private Integer age;
    private String place;

    @Enumerated(EnumType.STRING)
    private IdCardType idCardType;

    private String documentPath;
    private LocalDateTime createdAt = LocalDateTime.now();

    public enum IdCardType { AADHAAR, PAN }

    // Getters and Setters
    public Long getId() { return id; }
    public String getUniqueUhid() { return uniqueUhid; }
    public void setUniqueUhid(String uniqueUhid) { this.uniqueUhid = uniqueUhid; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }
    public String getPlace() { return place; }
    public void setPlace(String place) { this.place = place; }
    public IdCardType getIdCardType() { return idCardType; }
    public void setIdCardType(IdCardType idCardType) { this.idCardType = idCardType; }
    public String getDocumentPath() { return documentPath; }
    public void setDocumentPath(String documentPath) { this.documentPath = documentPath; }
}