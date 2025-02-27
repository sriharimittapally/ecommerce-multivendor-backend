package com.projects.ecommerce.multivendor.repo;

import com.projects.ecommerce.multivendor.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepo extends JpaRepository<Review, Long> {
    List<Review> findByProductId(Long productId);
}
