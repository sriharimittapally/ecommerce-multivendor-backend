package com.projects.ecommerce.multivendor.repo;

import com.projects.ecommerce.multivendor.model.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponRepo extends JpaRepository<Coupon, Long> {
    Coupon findByCode(String code);
}
