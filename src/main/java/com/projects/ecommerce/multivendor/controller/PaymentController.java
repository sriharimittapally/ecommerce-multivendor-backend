package com.projects.ecommerce.multivendor.controller;

import com.projects.ecommerce.multivendor.model.*;
import com.projects.ecommerce.multivendor.response.ApiResponse;
import com.projects.ecommerce.multivendor.response.PaymentLinkResponse;
import com.projects.ecommerce.multivendor.service.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    private final PaymentService paymentService;
    private final UserService userService;
    private final SellerService sellerService;
    private final SellerReportService sellerReportService;
    private final TransactionService transactionService;

    public PaymentController(PaymentService paymentService, UserService userService, SellerService sellerService, SellerReportService sellerReportService, TransactionService transactionService) {
        this.paymentService = paymentService;
        this.userService = userService;
        this.sellerService = sellerService;
        this.sellerReportService = sellerReportService;
        this.transactionService = transactionService;
    }

    @PostMapping("/{paymentId}")
    public ResponseEntity<ApiResponse> paymentSuccessHandler(
            @PathVariable String paymentId,
            @RequestParam String paymentLinkId,
            @RequestHeader("Authorization") String jwt) throws Exception {

        System.out.println("jwt in payment Contorller"+jwt);

        User user = userService.findUserByJwtToken(jwt);

        PaymentLinkResponse paymentResponse;

        PaymentOrder paymentOrder = paymentService.getPaymentOrderByPaymentId(paymentLinkId);


        boolean paymentSuccess = paymentService.ProceedPaymentOrder(
                paymentOrder,
                paymentId,
                paymentLinkId
        );
        if (paymentSuccess) {
            for (Order order : paymentOrder.getOrders()) {
                transactionService.createTransaction(order);
                System.out.println("created transaction called succesfully");
                Seller seller = sellerService.getSellerById(order.getSellerId());
                SellerReport report = sellerReportService.getSellerReport(seller);
                report.setCanceledOrders(report.getTotalOrders() + 1);
                report.setTotalEarnings(report.getTotalEarnings() + order.getTotalSellingPrice());
                report.setTotalSales(report.getTotalSales()+order.getOrderItems().size());
                sellerReportService.updateSellerReport(report);
            }
        }
        ApiResponse res = new ApiResponse();
        res.setMessage("Payment successful");
        return new ResponseEntity<>(res, HttpStatus.CREATED);

    }

}
