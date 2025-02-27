package com.projects.ecommerce.multivendor.service;

import com.projects.ecommerce.multivendor.domain.AccountStatus;
import com.projects.ecommerce.multivendor.exceptions.SellerException;
import com.projects.ecommerce.multivendor.model.Seller;

import java.util.List;

public interface SellerService {


    Seller getSellerProfile(String jwt) throws Exception;
    Seller createSeller(Seller seller) throws Exception;
    Seller getSellerById(Long id) throws SellerException;
    Seller getSellerByEmail(String email) throws Exception;
    List<Seller> getAllSellers(AccountStatus status);
    Seller updateSeller(Long id, Seller seller) throws SellerException;
    void deleteSeller(Long id) throws Exception;
    Seller verifyEmail(String email, String otp) throws Exception;
    Seller updateSellerAccountStatus(Long id, AccountStatus status) throws Exception;

}
