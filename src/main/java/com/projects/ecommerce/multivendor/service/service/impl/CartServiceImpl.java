package com.projects.ecommerce.multivendor.service.service.impl;

import com.projects.ecommerce.multivendor.model.Cart;
import com.projects.ecommerce.multivendor.model.CartItem;
import com.projects.ecommerce.multivendor.model.Product;
import com.projects.ecommerce.multivendor.model.User;
import com.projects.ecommerce.multivendor.repo.CartItemRepo;
import com.projects.ecommerce.multivendor.repo.CartRepo;
import com.projects.ecommerce.multivendor.repo.ProductRepo;
import com.projects.ecommerce.multivendor.service.CartService;

import org.springframework.stereotype.Service;

@Service
public class CartServiceImpl implements CartService {
    private final CartRepo cartRepo;
    private final ProductRepo productRepo;
    private final CartItemRepo cartItemRepo;

    public CartServiceImpl(CartRepo cartRepo, ProductRepo productRepo, CartItemRepo cartItemRepo) {
        this.cartRepo = cartRepo;
        this.productRepo = productRepo;
        this.cartItemRepo = cartItemRepo;
    }

    @Override
    public CartItem addCartItem(User user, Product product, String size, int quantity) {
        Cart cart = findUserCart(user);
        CartItem isPresent = cartItemRepo.findByCartAndProductAndSize(cart, product, size);

        if(isPresent == null) {
            CartItem cartItem = new CartItem();
            cartItem.setProduct(product);
            cartItem.setQuantity(quantity);
            cartItem.setUserId(user.getId());
            cartItem.setSize(size);

            int totalPrice = quantity * product.getSellingPrice();
            cartItem.setSellingPrice(totalPrice);
            cartItem.setMrpPrice(quantity * product.getMrpPrice());

            cart.getCartItems().add(cartItem);
            cartItem.setCart(cart);


            cartRepo.save(cart);
            return cartItemRepo.save(cartItem);
        }
        return isPresent;
    }




    @Override
    public Cart findUserCart(User user) {
        Cart cart = cartRepo.findByUserId(user.getId());

        if (cart == null) {
            cart = new Cart();
            cart.setUser(user);
            cartRepo.save(cart);
        }

        if (cart.getCartItems() == null) {
            throw new IllegalArgumentException("Cart Items not found for user: " + user.getId());
        }

        int totalPrice = 0;
        int totalDiscountedPrice = 0;
        int totalItem = 0;

        for (CartItem cartItem : cart.getCartItems()) {
            totalPrice += cartItem.getMrpPrice();
            totalDiscountedPrice += cartItem.getSellingPrice();
            totalItem += cartItem.getQuantity();
        }

        cart.setTotalMrpPrice(totalPrice);
        cart.setTotalItem(totalItem);
        cart.setTotalSellingPrice(totalDiscountedPrice);
        cart.setDiscount(calculateDiscountPercentage(totalPrice, totalDiscountedPrice));

        return cart;
    }


    private int calculateDiscountPercentage(int mrpPrice, int sellerPrice) {
        if(mrpPrice <= 0) {
           return 0;
        }
        double discount = mrpPrice-sellerPrice;
        double discountPercentage = (discount/mrpPrice)*100;
        return (int)discountPercentage;
    }
}
