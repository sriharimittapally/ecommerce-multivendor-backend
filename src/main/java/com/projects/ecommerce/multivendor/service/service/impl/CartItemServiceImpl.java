package com.projects.ecommerce.multivendor.service.service.impl;

import com.projects.ecommerce.multivendor.model.CartItem;
import com.projects.ecommerce.multivendor.model.User;
import com.projects.ecommerce.multivendor.repo.CartItemRepo;
import com.projects.ecommerce.multivendor.service.CartItemService;

import org.springframework.stereotype.Service;

@Service
public class CartItemServiceImpl implements CartItemService {

    public CartItemServiceImpl(CartItemRepo cartItemRepo) {
        this.cartItemRepo = cartItemRepo;
    }

    private final CartItemRepo cartItemRepo;

    @Override
    public CartItem updateCartItem(Long userId, Long id, CartItem cartItem) throws Exception {
        CartItem item = cartItemRepo.findById(id).orElseThrow(() -> new Exception("Cart item not found with id " + id));

        if (item.getCart().getUser().getId().equals(userId)) {
            item.setQuantity(cartItem.getQuantity());
            item.setMrpPrice(item.getQuantity() * item.getProduct().getMrpPrice());
            item.setSellingPrice(item.getQuantity() * item.getProduct().getSellingPrice());
            return cartItemRepo.save(item);
        }
        throw new Exception("You cannot update this cart item");
    }


    @Override
    public void removeCartItem(Long userId, Long cartItemId) throws Exception {
        CartItem cartItem = cartItemRepo.findById(cartItemId).orElseThrow(() -> new Exception("Cart item not found with id " + cartItemId));

        if (cartItem.getCart().getUser().getId().equals(userId)) {
            cartItemRepo.delete(cartItem);
        } else {
            throw new Exception("You cannot delete this item");
        }
    }



    @Override
    public CartItem findCartItemById(Long id) throws Exception {

        return cartItemRepo.findById(id).orElseThrow(()->
                new Exception("Cart item not found with id "+id));
    }
}

