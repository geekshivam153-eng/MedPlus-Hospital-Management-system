package com.hospital.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "billing_items")
public class BillingItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admission_id")
    private Admission admission;

    @Enumerated(EnumType.STRING)
    private ItemCategory itemCategory;

    private String description;
    private BigDecimal amount;

    public enum ItemCategory {
        DOCTOR_FEE, HOSPITAL_CHARGE, MEDICINE, SCAN_MRI, SCAN_CT, XRAY, MISC
    }

    // Getters and Setters
    public Long getId() { return id; }
    public Admission getAdmission() { return admission; }
    public void setAdmission(Admission admission) { this.admission = admission; }
    public ItemCategory getItemCategory() { return itemCategory; }
    public void setItemCategory(ItemCategory itemCategory) { this.itemCategory = itemCategory; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
}