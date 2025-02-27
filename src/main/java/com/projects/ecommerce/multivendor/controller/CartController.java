package com.projects.ecommerce.multivendor.controller;

import com.projects.ecommerce.multivendor.exceptions.ProductException;
import com.projects.ecommerce.multivendor.model.Cart;
import com.projects.ecommerce.multivendor.model.CartItem;
import com.projects.ecommerce.multivendor.model.Product;
import com.projects.ecommerce.multivendor.model.User;
import com.projects.ecommerce.multivendor.request.AddItemRequest;
import com.projects.ecommerce.multivendor.response.ApiResponse;
import com.projects.ecommerce.multivendor.service.CartItemService;
import com.projects.ecommerce.multivendor.service.CartService;
import com.projects.ecommerce.multivendor.service.ProductService;
import com.projects.ecommerce.multivendor.service.UserService;
import jdk.jshell.spi.ExecutionControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
public class CartController {
    private final CartService cartService;
    private final CartItemService cartItemService;
    private final UserService userService;
    private final ProductService productService;

    public CartController(CartService cartService, CartItemService cartItemService, UserService userService, ProductService productService) {
        this.cartService = cartService;
        this.cartItemService = cartItemService;
        this.userService = userService;
        this.productService = productService;
    }





    @GetMapping
    public ResponseEntity<Cart> findUserCartHandler(@RequestHeader("Authorization") String jwt) throws ExecutionControl.UserException ,Exception {

        User user = userService.findUserByJwtToken(jwt);

        Cart cart = cartService.findUserCart(user);

        return new ResponseEntity<Cart>(cart, HttpStatus.OK);
    }





    @PutMapping("/add")
    public ResponseEntity<CartItem> addItemToCart(
            @RequestBody AddItemRequest req,
            @RequestHeader("Authorization") String jwt) throws ProductException, Exception {
        User user = userService.findUserByJwtToken(jwt);
        Product product = productService.findProductById(req.getProductId());

        CartItem item = cartService.addCartItem(user,
                product,
                req.getSize(),
                req.getQuantity()
        );

        ApiResponse res = new ApiResponse();
        res.setMessage("Item added to cart Successfully");
        return new ResponseEntity<>(item, HttpStatus.ACCEPTED);
    }





    @DeleteMapping("/item/{cartItemId}")
    public ResponseEntity<ApiResponse> deleteCartItemHandler(
            @PathVariable Long cartItemId,
            @RequestHeader("Authorization") String jwt) throws ProductException, Exception {

        User user = userService.findUserByJwtToken(jwt);
        cartItemService.removeCartItem(user.getId(), cartItemId);
        ApiResponse res = new ApiResponse();
        res.setMessage("Item removed from cart Successfully");
        return new ResponseEntity<>(res, HttpStatus.ACCEPTED);
    }




    @PutMapping("/item/{cartItemId}")
    public ResponseEntity<CartItem> updateCartItemHandler(
            @PathVariable Long cartItemId,
            @RequestBody CartItem cartItem,
            @RequestHeader("Authorization") String jwt
    ) throws ProductException, Exception {
        User user = userService.findUserByJwtToken(jwt);
        CartItem updateCartItem = null;
        if(cartItem.getQuantity()>0){
            updateCartItem = cartItemService.updateCartItem(user.getId(),
                    cartItemId, cartItem);
        }
        return new ResponseEntity<>(updateCartItem, HttpStatus.ACCEPTED);
    }
}
