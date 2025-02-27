package com.projects.ecommerce.multivendor.service.service.impl;

import com.projects.ecommerce.multivendor.config.JwtProvider;
import com.projects.ecommerce.multivendor.domain.AccountStatus;
import com.projects.ecommerce.multivendor.domain.USER_ROLE;
import com.projects.ecommerce.multivendor.exceptions.SellerException;
import com.projects.ecommerce.multivendor.model.Address;
import com.projects.ecommerce.multivendor.model.Seller;
import com.projects.ecommerce.multivendor.repo.AddressRepo;
import com.projects.ecommerce.multivendor.repo.SellerRepo;
import com.projects.ecommerce.multivendor.service.SellerService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SellerServiceImpl implements SellerService {

    private final SellerRepo sellerRepo;
    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;
    private final AddressRepo addressRepo;


    public SellerServiceImpl(SellerRepo sellerRepo, JwtProvider jwtProvider, PasswordEncoder passwordEncoder, AddressRepo addressRepo ) {
        this.sellerRepo = sellerRepo;
        this.jwtProvider = jwtProvider;
        this.passwordEncoder = passwordEncoder;
        this.addressRepo = addressRepo;
    }

    @Override
    public Seller getSellerProfile(String jwt) throws Exception {
        String email = jwtProvider.getEmailFromJwtToken(jwt);
        return this.getSellerByEmail(email);
    }

    @Override
    public Seller createSeller(Seller seller) throws Exception {
        Seller sellerExist = sellerRepo.findByEmail(seller.getEmail());
        if(sellerExist != null) {
            throw new Exception("Seller already exists with this email");
        }

        Address savedAddress  = addressRepo.save(seller.getPickupAddress());

        Seller newSeller = new Seller();

        newSeller.setEmail(seller.getEmail());
        newSeller.setPassword(passwordEncoder.encode(seller.getPassword()));
        newSeller.setSellerName(seller.getSellerName());
        newSeller.setPickupAddress(savedAddress);
        newSeller.setGSTIN(seller.getGSTIN());
        newSeller.setRole(USER_ROLE.ROLE_SELLER);
        newSeller.setMobile(seller.getMobile());
        newSeller.setBankDetails(seller.getBankDetails());
        newSeller.setBusinessDetails(seller.getBusinessDetails());

        return sellerRepo.save(newSeller);
    }

    @Override
    public Seller getSellerById(Long id) throws SellerException{
        return sellerRepo.findById(id)
                .orElseThrow(()-> new SellerException("Seller not found with Id "+id));
    }

    @Override
    public Seller getSellerByEmail(String email) throws Exception {
        Seller seller = sellerRepo.findByEmail(email);
        if(seller == null) {
            throw new Exception("Seller not found!");
        }
        return seller;
    }

    @Override
    public List<Seller> getAllSellers(AccountStatus status) {
        return sellerRepo.findByAccountStatus(status);
    }

    @Override
    public Seller updateSeller(Long id, Seller seller) throws SellerException {

        Seller existingSeller = this.getSellerById(id);

        if(seller.getSellerName() != null) {
            existingSeller.setSellerName(seller.getSellerName());
        }
        if(seller.getMobile() != null) {
            existingSeller.setMobile(seller.getMobile());
        }
        if(seller.getEmail() != null) {
            existingSeller.setEmail(seller.getEmail());
        }
        if(seller.getBusinessDetails() != null && seller.getBusinessDetails().getBusinessName() != null) {
            existingSeller.getBusinessDetails().setBusinessName(
                    seller.getBusinessDetails().getBusinessName()
            );
        }
        if(seller.getBankDetails() !=null
            && seller.getBankDetails().getAccountHolderName() !=null
            && seller.getBankDetails().getIfscCode() !=null
            && seller.getBankDetails().getAccountNumber() !=null)
        {
            existingSeller.getBankDetails().setAccountHolderName(
                    seller.getBankDetails().getAccountHolderName()
            );
            existingSeller.getBankDetails().setAccountNumber(
                    seller.getBankDetails().getAccountNumber()
            );
            existingSeller.getBankDetails().setIfscCode(
                    seller.getBankDetails().getIfscCode()
            );
        }
        if(seller.getPickupAddress()!=null
            && seller.getPickupAddress().getAddress() !=null
            && seller.getPickupAddress().getMobile() !=null
            && seller.getPickupAddress().getCity() !=null
            && seller.getPickupAddress().getState() !=null)
        {
          existingSeller.getPickupAddress().setAddress(
                  seller.getPickupAddress().getAddress()
          );
          existingSeller.getPickupAddress().setMobile(
                  seller.getPickupAddress().getMobile()
          );
          existingSeller.getPickupAddress().setCity(
                  seller.getPickupAddress().getCity()
          );
          existingSeller.getPickupAddress().setState(
                  seller.getPickupAddress().getState()
          );
        }
        if(seller.getGSTIN()!=null){
            existingSeller.setGSTIN(seller.getGSTIN());
        }
        return sellerRepo.save(existingSeller);

    }

    @Override
    public void deleteSeller(Long id) throws Exception{
        Seller seller = getSellerById(id);
        sellerRepo.delete(seller);

    }

    @Override
    public Seller verifyEmail(String email, String otp) throws Exception {
        Seller seller = getSellerByEmail(email);
        seller.setEmailVerified(true);
        return sellerRepo.save(seller);
    }

    @Override
    public Seller updateSellerAccountStatus(Long id, AccountStatus status) throws Exception {
        Seller seller = getSellerById(id);
        seller.setEmailVerified(true);
        return sellerRepo.save(seller);
    }
}
