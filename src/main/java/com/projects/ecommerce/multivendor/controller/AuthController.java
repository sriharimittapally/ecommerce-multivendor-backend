package com.projects.ecommerce.multivendor.controller;

import com.projects.ecommerce.multivendor.domain.USER_ROLE;
import com.projects.ecommerce.multivendor.model.User;
import com.projects.ecommerce.multivendor.model.VerificationCode;
import com.projects.ecommerce.multivendor.repo.UserRepo;
import com.projects.ecommerce.multivendor.repo.VerificationCodeRepo;
import com.projects.ecommerce.multivendor.request.LoginOtpRequest;
import com.projects.ecommerce.multivendor.request.LoginRequest;
import com.projects.ecommerce.multivendor.response.ApiResponse;
import com.projects.ecommerce.multivendor.response.AuthResponse;
import com.projects.ecommerce.multivendor.response.SignupRequest;
import com.projects.ecommerce.multivendor.service.AuthService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {




    private final AuthService authService;
    private final VerificationCodeRepo verificationCodeRepo;

    public AuthController(AuthService authService, VerificationCodeRepo verificationCodeRepo) {

        this.authService = authService;
        this.verificationCodeRepo = verificationCodeRepo;
    }

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> createUserHandler (@RequestBody SignupRequest req) throws Exception {

        String jwt=authService.createUser(req);
//
        AuthResponse res = new AuthResponse();
        res.setJwt(jwt);
        res.setMessage("Registered Successfully");
        res.setRole(USER_ROLE.ROLE_CUSTOMER);
        return ResponseEntity.ok(res);
    }


    @PostMapping("/send/login-signup-otp")
    public ResponseEntity<ApiResponse> sendOtpHandler (@RequestBody LoginOtpRequest req) throws Exception {

        authService.sendLoginAndSignupOtp(req.getEmail(), req.getRole());

        ApiResponse res = new ApiResponse();

        res.setMessage("OTP sent Successfully");

        return ResponseEntity.ok(res);

    }


    @PostMapping("/signin")
    public ResponseEntity<AuthResponse> loginHandler (@RequestBody LoginRequest req) throws Exception {

        AuthResponse authResponse = authService.signing(req);
        return ResponseEntity.ok(authResponse);


    }

}
