package com.projects.ecommerce.multivendor.service;

import com.projects.ecommerce.multivendor.model.Product;
import com.projects.ecommerce.multivendor.model.User;
import com.projects.ecommerce.multivendor.model.Wishlist;

public interface WishlistService {
    Wishlist createWishlist(User user);
    Wishlist getWishlistByUserId(User user);
    Wishlist addProductToWishlist(User user, Product product);
}
