package com.projects.ecommerce.multivendor.repo;

import com.projects.ecommerce.multivendor.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepo extends JpaRepository<Order, Long> {

    List<Order> findByUserId(long userId);
    List<Order> findBySellerId(long sellerId);}
