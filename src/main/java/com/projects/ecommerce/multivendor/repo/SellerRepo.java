package com.projects.ecommerce.multivendor.repo;

import com.projects.ecommerce.multivendor.domain.AccountStatus;
import com.projects.ecommerce.multivendor.model.Seller;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SellerRepo  extends JpaRepository<Seller, Long> {
    Seller findByEmail(String email);
    List<Seller> findByAccountStatus(AccountStatus status);
}
