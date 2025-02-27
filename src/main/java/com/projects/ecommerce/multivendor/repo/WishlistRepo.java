package com.projects.ecommerce.multivendor.repo;

import com.projects.ecommerce.multivendor.model.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WishlistRepo extends JpaRepository<Wishlist, Long> {

    Wishlist findByUserId(Long userId);
}
