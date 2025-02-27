package com.projects.ecommerce.multivendor.repo;

import com.projects.ecommerce.multivendor.model.SellerReport;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SellerReportRepo extends JpaRepository<SellerReport, Long> {
    SellerReport findBySellerId(Long id);
}
