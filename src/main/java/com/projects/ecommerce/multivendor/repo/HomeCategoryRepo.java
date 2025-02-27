package com.projects.ecommerce.multivendor.repo;

import com.projects.ecommerce.multivendor.model.HomeCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HomeCategoryRepo extends JpaRepository<HomeCategory, Long> {
}
