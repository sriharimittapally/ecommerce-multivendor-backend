package com.projects.ecommerce.multivendor.service;

import com.projects.ecommerce.multivendor.model.Order;
import com.projects.ecommerce.multivendor.model.Seller;
import com.projects.ecommerce.multivendor.model.Transaction;

import java.util.List;

public interface TransactionService {
    Transaction createTransaction(Order order);
    List<Transaction> getTransactionsBySellerId(Seller seller);
    List<Transaction> getAllTransactions();
}
