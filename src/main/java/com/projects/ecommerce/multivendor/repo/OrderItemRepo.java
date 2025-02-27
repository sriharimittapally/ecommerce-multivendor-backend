package com.projects.ecommerce.multivendor.repo;


import com.projects.ecommerce.multivendor.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepo extends JpaRepository<OrderItem, Long> {

}
