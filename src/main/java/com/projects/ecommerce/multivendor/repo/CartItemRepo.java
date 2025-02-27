package com.projects.ecommerce.multivendor.repo;

import com.projects.ecommerce.multivendor.model.Cart;
import com.projects.ecommerce.multivendor.model.CartItem;
import com.projects.ecommerce.multivendor.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepo  extends JpaRepository<CartItem, Long> {
    CartItem findByCartAndProductAndSize(Cart cart, Product product, String size);
}
