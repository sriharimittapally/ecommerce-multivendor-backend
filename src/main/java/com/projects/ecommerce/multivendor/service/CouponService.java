package com.projects.ecommerce.multivendor.service;

import com.projects.ecommerce.multivendor.model.Cart;
import com.projects.ecommerce.multivendor.model.Coupon;
import com.projects.ecommerce.multivendor.model.User;

import java.util.List;

public interface CouponService {
    Cart applyCoupon(String code, double orderValue, User user) throws Exception;
    Cart removeCoupon(String code, User user) throws Exception;
    Coupon findCouponById(Long id) throws Exception;
    Coupon  createCoupon(Coupon coupon);
    List<Coupon> findAllCoupons();
    void deleteCoupon(Long id) throws Exception;
}
