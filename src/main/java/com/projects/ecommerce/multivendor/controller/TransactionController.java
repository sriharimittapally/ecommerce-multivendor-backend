package com.projects.ecommerce.multivendor.controller;

import com.projects.ecommerce.multivendor.model.Seller;
import com.projects.ecommerce.multivendor.model.Transaction;
import com.projects.ecommerce.multivendor.service.SellerService;
import com.projects.ecommerce.multivendor.service.TransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService transactionService;
    private final SellerService sellerService;

    public TransactionController(TransactionService transactionService, SellerService sellerService) {
        this.transactionService = transactionService;
        this.sellerService = sellerService;
    }


    @GetMapping("/seller")
    public ResponseEntity<List<Transaction>> getTransactionsBySellerId(@RequestHeader("Authorization") String jwt) throws Exception {

        Seller seller = sellerService.getSellerProfile(jwt);
        System.out.println("Seller: " + seller);

        List<Transaction> transactions = transactionService.getTransactionsBySellerId(seller);
        System.out.println("transactions: " + transactions);
        return ResponseEntity.ok(transactions);

    }

    @GetMapping
    public ResponseEntity<List<Transaction>> getAllTransactions(){
        List<Transaction> transactions = transactionService.getAllTransactions();
        return ResponseEntity.ok(transactions);
    }
}
