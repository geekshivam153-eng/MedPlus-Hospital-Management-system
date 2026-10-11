package com.hospital.repository;

import com.hospital.entity.BillingItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BillingItemRepository extends JpaRepository<BillingItem, Long> {
}
