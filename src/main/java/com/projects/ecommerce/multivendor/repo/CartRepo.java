package com.projects.ecommerce.multivendor.repo;

import com.projects.ecommerce.multivendor.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepo extends JpaRepository<Cart, Long> {
    Cart findByUserId(Long id);
}
