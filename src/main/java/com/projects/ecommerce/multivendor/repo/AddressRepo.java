package com.projects.ecommerce.multivendor.repo;

import com.projects.ecommerce.multivendor.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepo extends JpaRepository<Address, Long> {
}
