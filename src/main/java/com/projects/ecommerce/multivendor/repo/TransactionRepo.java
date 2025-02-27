package com.projects.ecommerce.multivendor.repo;

import com.projects.ecommerce.multivendor.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepo extends JpaRepository<Transaction, Long> {
    List<Transaction> findBySellerId(Long sellerId);
}
