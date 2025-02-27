package com.projects.ecommerce.multivendor.service.service.impl;

import com.projects.ecommerce.multivendor.model.Order;
import com.projects.ecommerce.multivendor.model.Seller;
import com.projects.ecommerce.multivendor.model.Transaction;
import com.projects.ecommerce.multivendor.repo.SellerRepo;
import com.projects.ecommerce.multivendor.repo.TransactionRepo;
import com.projects.ecommerce.multivendor.service.TransactionService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepo transactionRepo;
    private final SellerRepo sellerRepo;

    public TransactionServiceImpl(TransactionRepo transactionRepo, SellerRepo sellerRepo) {
        this.transactionRepo = transactionRepo;
        this.sellerRepo = sellerRepo;
    }

    @Override
    public Transaction createTransaction(Order order) {
        Seller seller = sellerRepo.findById(order.getSellerId()).get();

        Transaction transaction = new Transaction();
        transaction.setSeller(seller);
        transaction.setCustomer(order.getUser());
        transaction.setOrder(order);
    System.out.println("transaction:" +transaction);
        return transactionRepo.save(transaction);

    }

    @Override
    public List<Transaction> getTransactionsBySellerId(Seller seller) {
        return transactionRepo.findBySellerId(seller.getId());
    }

    @Override
    public List<Transaction> getAllTransactions() {
        return transactionRepo.findAll();
    }
}
