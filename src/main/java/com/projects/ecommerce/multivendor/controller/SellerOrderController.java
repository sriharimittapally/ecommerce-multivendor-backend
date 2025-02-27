package com.projects.ecommerce.multivendor.controller;

import com.projects.ecommerce.multivendor.domain.OrderStatus;
import com.projects.ecommerce.multivendor.exceptions.SellerException;
import com.projects.ecommerce.multivendor.model.Order;
import com.projects.ecommerce.multivendor.model.Seller;
import com.projects.ecommerce.multivendor.service.OrderService;
import com.projects.ecommerce.multivendor.service.SellerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/seller/orders")
public class SellerOrderController {

    private final OrderService orderService;
    private final SellerService sellerService;

    public SellerOrderController(OrderService orderService, SellerService sellerService) {
        this.orderService = orderService;
        this.sellerService = sellerService;
    }

    @GetMapping
    public ResponseEntity<List<Order>> getAllOrdersHandler(
            @RequestHeader("Authorization") String jwt

    ) throws Exception {
        Seller seller = sellerService.getSellerProfile(jwt);
        List<Order>  orders = orderService.sellersOrder(seller.getId());

        return new ResponseEntity<>(orders, HttpStatus.ACCEPTED);
    }



    @PatchMapping("/{orderId}/status/{orderStatus}")
    public ResponseEntity<Order> updateOrderHandler(
            @RequestHeader("Authorization")
            String jwt,
            @PathVariable Long orderId,
            @PathVariable OrderStatus orderStatus
    ) throws Exception {

        Order order = orderService.updateOrderStatus(orderId, orderStatus);
        return new ResponseEntity<>(order, HttpStatus.ACCEPTED);
    }
}
