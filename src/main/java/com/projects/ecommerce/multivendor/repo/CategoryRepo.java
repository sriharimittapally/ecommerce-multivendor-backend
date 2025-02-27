package com.projects.ecommerce.multivendor.repo;

import com.projects.ecommerce.multivendor.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepo extends JpaRepository<Category, Long> {
    Category findByCategoryId(String categoryId);
}
