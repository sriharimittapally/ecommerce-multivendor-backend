package com.projects.ecommerce.multivendor.service.service.impl;

import com.projects.ecommerce.multivendor.domain.OrderStatus;
import com.projects.ecommerce.multivendor.domain.PaymentStatus;
import com.projects.ecommerce.multivendor.model.*;
import com.projects.ecommerce.multivendor.repo.AddressRepo;
import com.projects.ecommerce.multivendor.repo.OrderItemRepo;
import com.projects.ecommerce.multivendor.repo.OrderRepo;
import com.projects.ecommerce.multivendor.service.OrderService;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepo orderRepo;
    private final AddressRepo addressRepo;
    private final OrderItemRepo orderItemRepo;

    public OrderServiceImpl(OrderRepo orderRepo, AddressRepo addressRepo, OrderItemRepo orderItemRepo) {
        this.orderRepo = orderRepo;
        this.addressRepo = addressRepo;
        this.orderItemRepo = orderItemRepo;
    }


    @Override
    public Set<Order> createOrder(User user, Address shippingAddress, Cart cart) {
        if(!user.getAddresses().contains(shippingAddress)) {
            user.getAddresses().add(shippingAddress);
        }
        Address address = addressRepo.save(shippingAddress);

        // brand 1 => 4 shirts
        // brand 2 => 3 pants
        // brand 3 => 1 watch

        Map<Long, List<CartItem>> itemsBySeller = cart.getCartItems().stream()
                .collect(Collectors.groupingBy(item->item.getProduct().getSeller().getId()));
        Set<Order> orders = new HashSet<>();

        for (Map.Entry<Long, List<CartItem>> entry : itemsBySeller.entrySet()) {
            Long sellerId = entry.getKey();
            List<CartItem> items = entry.getValue();

            int totalOrderPrice = items.stream().mapToInt(
                    CartItem::getSellingPrice
            ).sum();
            int totalItem= items.stream().mapToInt(CartItem::getQuantity).sum();

            Order createdOrder = new Order();
            createdOrder.setUser(user);
            createdOrder.setSellerId(sellerId);
            createdOrder.setTotalMrpPrice(totalOrderPrice);
            createdOrder.setTotalItem(totalItem);
            createdOrder.setTotalSellingPrice(totalOrderPrice);
            createdOrder.setShippingAddress(address);
            createdOrder.setOrderStatus(OrderStatus.PENDING);
            createdOrder.getPaymentDetails().setStatus(PaymentStatus.PENDING);

            Order savedOrder = orderRepo.save(createdOrder);
            orders.add(savedOrder);

            List<OrderItem> orderItems = new ArrayList<>();
            for(CartItem item : items) {
                OrderItem orderItem = new OrderItem();
                orderItem.setOrder(savedOrder);
                orderItem.setMrpPrice(item.getMrpPrice());
                orderItem.setQuantity(item.getQuantity());
                orderItem.setProduct(item.getProduct());
                orderItem.setSize(item.getSize());
                orderItem.setUserId(user.getId());
                orderItem.setSellingPrice(item.getSellingPrice());
                savedOrder.getOrderItems().add(orderItem);

                OrderItem savedOrderItem = orderItemRepo.save(orderItem);
                orderItems.add(savedOrderItem);

            }
        }

        return orders;
    }

    @Override
    public Order findOrderById(Long id) throws Exception {
        return orderRepo.findById(id).orElseThrow(()->
                new Exception("Order Not Found..."));
    }

    @Override
    public List<Order> usersOrderHistory(Long userId) {
        return orderRepo.findByUserId(userId);
    }

    @Override
    public List<Order> sellersOrder(Long sellerId) {
        return orderRepo.findBySellerId(sellerId);
    }

    @Override
    public Order updateOrderStatus(Long orderId, OrderStatus orderStatus) throws Exception {
        Order order = findOrderById(orderId);
        order.setOrderStatus(orderStatus);
        return orderRepo.save(order);
    }

    @Override
    public Order cancelOrder(Long orderId, User user) throws Exception {
        Order order = findOrderById(orderId);
        if(!user.getId().equals(order.getUser().getId())) {
            throw new Exception("You do not have access to this order");
        }
        order.setOrderStatus(OrderStatus.CANCELLED);
        return orderRepo.save(order);
    }

    @Override
    public OrderItem getOrderItemById(Long id) throws Exception {
        return orderItemRepo.findById(id).orElseThrow(()->
                new Exception("Order Item does not exists....!"));
    }

}
