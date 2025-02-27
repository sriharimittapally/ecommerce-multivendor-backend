package com.projects.ecommerce.multivendor.service;

import com.projects.ecommerce.multivendor.domain.USER_ROLE;
import com.projects.ecommerce.multivendor.request.LoginRequest;
import com.projects.ecommerce.multivendor.response.AuthResponse;
import com.projects.ecommerce.multivendor.response.SignupRequest;

public interface AuthService {

    void sendLoginAndSignupOtp(String email, USER_ROLE role) throws Exception;

    String createUser(SignupRequest req) throws Exception;

    AuthResponse signing(LoginRequest req) throws Exception;
}
