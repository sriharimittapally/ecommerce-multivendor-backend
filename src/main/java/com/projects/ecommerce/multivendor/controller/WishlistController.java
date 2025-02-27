package com.projects.ecommerce.multivendor.controller;

import com.projects.ecommerce.multivendor.model.Product;
import com.projects.ecommerce.multivendor.model.User;
import com.projects.ecommerce.multivendor.model.Wishlist;
import com.projects.ecommerce.multivendor.service.ProductService;
import com.projects.ecommerce.multivendor.service.UserService;
import com.projects.ecommerce.multivendor.service.WishlistService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/wishlist")
public class WishlistController {
    private final WishlistService wishlistService;
    private final UserService userService;
    private final ProductService productService;

    public WishlistController(WishlistService wishlistService, UserService userService, ProductService productService) {
        this.wishlistService = wishlistService;
        this.userService = userService;
        this.productService = productService;
    }

    @GetMapping()
    public ResponseEntity<Wishlist> getWishlistByUserId(@RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserByJwtToken(jwt);
        Wishlist wishlist = wishlistService.getWishlistByUserId(user);
        return ResponseEntity.ok(wishlist);
    }


    @PostMapping("/add-product/{productId}")
    public ResponseEntity<Wishlist> addProductToWishlist(
            @RequestHeader("Authorization") String jwt,
            @PathVariable Long productId) throws Exception {
        User user = userService.findUserByJwtToken(jwt);
        Product product = productService.findProductById(productId);
        Wishlist updatedWishlist = wishlistService.addProductToWishlist(user, product);
        return ResponseEntity.ok(updatedWishlist);
    }
}

