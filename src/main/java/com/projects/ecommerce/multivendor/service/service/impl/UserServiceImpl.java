package com.projects.ecommerce.multivendor.service.service.impl;

import com.projects.ecommerce.multivendor.config.JwtProvider;
import com.projects.ecommerce.multivendor.model.Address;
import com.projects.ecommerce.multivendor.model.User;
import com.projects.ecommerce.multivendor.repo.AddressRepo;
import com.projects.ecommerce.multivendor.repo.UserRepo;
import com.projects.ecommerce.multivendor.service.UserService;

import org.springframework.stereotype.Service;


@Service

public class UserServiceImpl implements UserService {

    private final UserRepo userRepo;
    private final JwtProvider jwtProvider;
    private final AddressRepo addressRepo;

    public UserServiceImpl(UserRepo userRepo, JwtProvider jwtProvider, AddressRepo addressRepo) {
        this.userRepo = userRepo;
        this.jwtProvider = jwtProvider;
        this.addressRepo = addressRepo;
    }

    @Override
    public User findUserByJwtToken(String jwt) throws Exception {
        String email = jwtProvider.getEmailFromJwtToken(jwt);


        return this.findUserByEmail(email);
    }

    @Override
    public User findUserByEmail(String email) throws Exception {
        User user = this.userRepo.findByEmail(email);
        if(user == null){
            throw new Exception("User not found with email - " +email);
        }
        return user;
    }

    @Override
    public Address saveAddress(User user, Address address) {
        user.getAddresses().add(address); // Add address to user's address list
        return addressRepo.save(address); // Save address in DB
    }

}



