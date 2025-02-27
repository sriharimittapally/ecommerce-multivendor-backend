package com.projects.ecommerce.multivendor.controller;

import com.projects.ecommerce.multivendor.domain.USER_ROLE;
import com.projects.ecommerce.multivendor.model.Address;
import com.projects.ecommerce.multivendor.model.User;
import com.projects.ecommerce.multivendor.response.AuthResponse;
import com.projects.ecommerce.multivendor.response.SignupRequest;
import com.projects.ecommerce.multivendor.service.UserService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController

public class UserController {
    private final UserService userService;


    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("api/users/profile")
    public ResponseEntity<User> userProfileHandler
            (
                    @RequestHeader("Authorization") String jwt
            ) throws Exception {
        System.out.println("jwt: " + jwt);
        User user = userService.findUserByJwtToken(jwt);

        return ResponseEntity.ok(user);

    }

    @PostMapping("api/users/address")
    public ResponseEntity<Address> saveAddress(
            @RequestHeader("Authorization") String jwt,
            @RequestBody Address address) throws Exception {

        User user = userService.findUserByJwtToken(jwt); // Get user from JWT
        Address savedAddress = userService.saveAddress(user, address); // Save address
        return ResponseEntity.ok(savedAddress);
    }



}

