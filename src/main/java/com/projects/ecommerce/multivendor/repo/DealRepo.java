package com.projects.ecommerce.multivendor.repo;

import com.projects.ecommerce.multivendor.model.Deal;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DealRepo extends JpaRepository<Deal, Long> {

}
