package com.projects.ecommerce.multivendor.service;


import com.projects.ecommerce.multivendor.model.Cart;
import com.projects.ecommerce.multivendor.model.CartItem;
import com.projects.ecommerce.multivendor.model.Product;
import com.projects.ecommerce.multivendor.model.User;

public interface CartService {
    public CartItem addCartItem(
            User user,
            Product product,
            String size,
            int quantity
            );
    public Cart findUserCart(User user);

}
