package com.projects.ecommerce.multivendor.service.service.impl;

import com.projects.ecommerce.multivendor.domain.PaymentOrderStatus;
import com.projects.ecommerce.multivendor.domain.PaymentStatus;
import com.projects.ecommerce.multivendor.model.Order;
import com.projects.ecommerce.multivendor.model.PaymentOrder;
import com.projects.ecommerce.multivendor.model.User;
import com.projects.ecommerce.multivendor.repo.OrderRepo;
import com.projects.ecommerce.multivendor.repo.PaymentOrderRepo;
import com.projects.ecommerce.multivendor.service.PaymentService;
import com.razorpay.Payment;
import com.razorpay.PaymentLink;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import org.json.JSONObject;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


import java.util.Set;

@Service
public class PaymentServiceImpl implements PaymentService {

    private final PaymentOrderRepo paymentOrderRepo;
    private final OrderRepo orderRepo;


    public PaymentServiceImpl(PaymentOrderRepo paymentOrderRepo, OrderRepo orderRepo) {
        this.paymentOrderRepo = paymentOrderRepo;
        this.orderRepo = orderRepo;
    }

    @Value("${razorpay.api.secret}")
    private String apiKey;
    @Value("${razorpay.api.key}")
    private String apiSecret;
    @Value("${stripe.api.key}")
    private String stripeSecretKey ;


    @Override
    public PaymentOrder createOrder(User user, Set<Order> orders) {
        Long amount =  orders.stream().mapToLong(Order::getTotalSellingPrice).sum();
        PaymentOrder paymentOrder = new PaymentOrder();
        paymentOrder.setAmount(amount);
        paymentOrder.setUser(user);
        paymentOrder.setOrders(orders);

        return paymentOrderRepo.save(paymentOrder);
    }

    @Override
    public PaymentOrder getPaymentOrderById(Long orderId) throws Exception {
        return paymentOrderRepo.findById(orderId).orElseThrow(()->
                 new Exception("Payment order not found...!"));
    }

    @Override
    public PaymentOrder getPaymentOrderByPaymentId(String orderId) throws Exception {
        PaymentOrder paymentOrder = paymentOrderRepo.findByPaymentLinkId(orderId);
        if (paymentOrder == null) {
            throw  new Exception("Payment  order not found with provided payment  linnk Id");
        }
        return paymentOrder;
    }

    @Override
    public Boolean ProceedPaymentOrder(PaymentOrder paymentOrder, String paymentId, String paymentLinkId) throws RazorpayException {
        if (paymentOrder.getStatus().equals(PaymentOrderStatus.PENDING)){
            RazorpayClient razorpay = new RazorpayClient(apiKey, apiSecret);
            Payment payment = razorpay.payments.fetch(paymentId);
            String status = payment.get("status");
            if(status.equals("captured")){
                Set<Order> orders = paymentOrder.getOrders();
                for (Order order : orders) {
                    order.setPaymentStatus(PaymentStatus.COMPLETED);
                    orderRepo.save(order);
                }
                paymentOrder.setStatus(PaymentOrderStatus.SUCCESS);
                paymentOrderRepo.save(paymentOrder);
                return true;
            }
            paymentOrder.setStatus(PaymentOrderStatus.FAILED);
            paymentOrderRepo.save(paymentOrder);
            return false;
        }
        return false;
    }

    @Override
    public PaymentLink createRazorpayPaymentLink(User user, Long amount, Long orderId) throws RazorpayException {
        amount = amount * 100;
        try{
            RazorpayClient razorpay = new RazorpayClient(apiKey, apiSecret);

            JSONObject paymentLinkRequest = new JSONObject();
            paymentLinkRequest.put("amount", amount);
            paymentLinkRequest.put("currency", "INR");

            JSONObject customer = new JSONObject();
            customer.put("name", user.getFullName());
            customer.put("email", user.getEmail());
            paymentLinkRequest.put("customer", customer);

            JSONObject notify = new JSONObject();
            notify.put("email", true);
            notify.put("sms", user.getMobile());
            paymentLinkRequest.put("notify", notify);

            paymentLinkRequest.put("callback_url", "http://localhost:5173/payment-success"+orderId);

            paymentLinkRequest.put("callback_method", "get");

            PaymentLink paymentLink = razorpay.paymentLink.create(paymentLinkRequest);

            String paymentLinkUrl = paymentLink.get("short_url");
            String paymentLinkId = paymentLink.get("id");

            return paymentLink;


        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new RazorpayException(e.getMessage());
        }

    }

    @Override
    public String createStripePaymentLink(User user, Long amount, Long orderId) throws StripeException {
        Stripe.apiKey = stripeSecretKey;


        SessionCreateParams params = SessionCreateParams.builder().addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl("http://localhost:5173/payment-success/"+orderId)
                .setCancelUrl("http://localhost:5173/payment-cancel")
                .addLineItem(SessionCreateParams.LineItem.builder()
                        .setQuantity(1L).setPriceData(SessionCreateParams.LineItem.PriceData.builder()
                                .setCurrency("inr")
                                .setUnitAmount(amount*100)
                                .setProductData(
                                        SessionCreateParams.LineItem.PriceData.ProductData
                                                .builder().setName("Myntra payment")
                                                .build()
                                ).build()
                        ).build()
                )
                .build();

        Session session = Session.create(params);
        return session.getUrl();
    }
}
