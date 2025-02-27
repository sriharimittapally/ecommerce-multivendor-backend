package com.projects.ecommerce.multivendor.service.service.impl;

import com.projects.ecommerce.multivendor.config.JwtProvider;
import com.projects.ecommerce.multivendor.domain.USER_ROLE;
import com.projects.ecommerce.multivendor.model.Cart;
import com.projects.ecommerce.multivendor.model.Seller;
import com.projects.ecommerce.multivendor.model.User;
import com.projects.ecommerce.multivendor.model.VerificationCode;
import com.projects.ecommerce.multivendor.repo.CartRepo;
import com.projects.ecommerce.multivendor.repo.UserRepo;
import com.projects.ecommerce.multivendor.repo.VerificationCodeRepo;
import com.projects.ecommerce.multivendor.request.LoginRequest;
import com.projects.ecommerce.multivendor.response.AuthResponse;
import com.projects.ecommerce.multivendor.repo.SellerRepo;
import com.projects.ecommerce.multivendor.response.SignupRequest;
import com.projects.ecommerce.multivendor.service.AuthService;
import com.projects.ecommerce.multivendor.service.EmailService;
import com.projects.ecommerce.multivendor.util.OtpUtil;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


@Service
public class AuthServiceImpl implements AuthService {
    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final CartRepo cartRepo;
    private final JwtProvider jwtProvider;
    private final VerificationCodeRepo verificationCodeRepo;
    private final EmailService emailService;
    private final CustomUserServiceImpl customUserService;
    private final SellerRepo sellerRepo;

    public AuthServiceImpl(UserRepo userRepo, PasswordEncoder passwordEncoder, CartRepo cartRepo, JwtProvider jwtProvider, VerificationCodeRepo verificationCodeRepo, EmailService emailService, CustomUserServiceImpl customUserService, SellerRepo sellerRepo) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.cartRepo = cartRepo;
        this.jwtProvider = jwtProvider;
        this.verificationCodeRepo = verificationCodeRepo;
        this.emailService = emailService;
        this.customUserService = customUserService;
        this.sellerRepo = sellerRepo;
    }


    @Override
    public void sendLoginAndSignupOtp(String email , USER_ROLE role) throws Exception {
        if (role == null) {
            throw new Exception("Role is required (CUSTOMER or SELLER)");
        }

        if (email == null || email.isEmpty()) {
            throw new Exception("Email is required");
        }

        // Only check for existence if role is SELLER
        if (role.equals(USER_ROLE.ROLE_SELLER)) {
            Seller seller = sellerRepo.findByEmail(email);
            if (seller == null) {
                throw new Exception("User or seller does not exist with the provided email");
            }
        }

        // Remove user existence check for signup, OTP should be sent to new users

        // Remove previous OTP if it exists
        VerificationCode isExist = verificationCodeRepo.findByEmail(email);
        if (isExist != null) {
            verificationCodeRepo.delete(isExist);
        }

        // Generate and store new OTP
        String otp = OtpUtil.generateOtp();
        VerificationCode verificationCode = new VerificationCode();
        verificationCode.setOtp(otp);
        verificationCode.setEmail(email);
        verificationCodeRepo.save(verificationCode);

        // Send email with OTP
        String subject = "ShopEasy Security Code – Do Not Share";
        String text = "<html><body style='font-family: Arial, sans-serif; background-color: #f4f4f4; padding: 10px; margin: 0;'>"
                + "<table role='presentation' width='100%' cellpadding='0' cellspacing='0' border='0' style='background: #f4f4f4; width: 100%;'>"
                + "<tr><td align='center' style='padding: 10px;'>"
                + "<table role='presentation' width='100%' cellpadding='0' cellspacing='0' border='0' style='background: white; width: 100%; padding: 10px; border-radius: 8px; box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);'>"
                + "<tr><td align='center' style='padding: 20px 0;'>"
                + "<h1 style='color: #0056b3; margin: 0; font-size: 24px;'>Verification code - ShopEasy</h1>"
                + "<p style='color: #777; font-size: 14px; margin: 5px 0;'>Your One-Time Password (OTP) for secure access</p>"
                + "</td></tr>"
                + "<tr><td align='center' style='padding: 15px 0;'>"
                + "<p style='font-size: 16px; color: #333; margin: 0;'>Enter the OTP below to proceed:</p>"
                + "<div style='font-size: 32px; font-weight: bold; color: white; background: #0056b3; padding: 15px 30px; border-radius: 8px; display: inline-block; letter-spacing: 2px; margin-top: 10px;'>"
                + otp + "</div>"
                + "</td></tr>"
                + "<tr><td align='center' style='padding: 15px 20px; margin-top:5px'>"
                + "<p style='font-size: 14px; color: #666; margin: 0;'>This OTP is valid for a limited time. Please do not share it with anyone.</p>"
                + "<p style='font-size: 14px; color: #d9534f; margin: 5px 0; font-weight: bold;'>If you did not request this OTP, please ignore this email or contact support immediately.</p>"
                + "</td></tr>"
                + "<tr><td align='center' style='padding: 20px 0;'>"
                + "<p style='font-size: 12px; color: #777; margin: 0;'>This is an automated email—please do not reply.</p>"
                + "<p style='font-size: 14px; color: #555; margin: 5px 0;'><strong>The ShopEasy Team</strong></p>"
                + "</td></tr>"
                + "</table></td></tr></table></body></html>";

        emailService.sendVerificationOtpEmail(email, otp, subject, text);
    }

    @Override
    public String createUser(SignupRequest req) throws Exception {


        VerificationCode verificationCode = verificationCodeRepo.findByEmail(req.getEmail());

        if (verificationCode == null || !verificationCode.getOtp().equals(req.getOtp())) {
            throw new Exception("Invalid OTP!");
        }

        User user = userRepo.findByEmail(req.getEmail());

        if(user == null) {
           User createdUser = new User();
           createdUser.setEmail(req.getEmail());
           createdUser.setFullName(req.getFullName());
           createdUser.setRole(USER_ROLE.ROLE_CUSTOMER);
           createdUser.setMobile("8978874510");
           createdUser.setPassword(passwordEncoder.encode(req.getOtp()));

           user = userRepo.save(createdUser);


           Cart cart = new Cart();
           cart.setUser(user);
           cartRepo.save(cart);
        }

        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(USER_ROLE.ROLE_CUSTOMER.toString()));

        Authentication authentication = new UsernamePasswordAuthenticationToken(req.getEmail(), null, authorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return jwtProvider.generateToken(authentication);
    }




    @Override
    public AuthResponse signing(LoginRequest req) throws Exception {
        String username = req.getEmail();
        String otp = req.getOtp();

        Authentication authentication = authenticate(username,otp);
        SecurityContextHolder
                .getContext().setAuthentication(authentication);
        String token = jwtProvider.generateToken(authentication);

        AuthResponse authResponse = new AuthResponse();
        authResponse.setJwt(token);
        authResponse.setMessage("Login successful");

        Collection<? extends  GrantedAuthority> authorities = authentication.getAuthorities();
        String roleName = authorities.isEmpty()?null:authorities.iterator().next().getAuthority();

        authResponse.setRole(USER_ROLE.valueOf(roleName));
        return authResponse;
    }



    private Authentication authenticate(String username, String otp) throws Exception {
        UserDetails userDetails = customUserService.loadUserByUsername(username);
        String SELLER_PREFIX = "seller_";
        if (username.startsWith(SELLER_PREFIX)) {
            username = username.substring(SELLER_PREFIX.length());
            }

        if(userDetails==null) {
            throw new Exception("Invalid username");
        }

        VerificationCode verificationCode = verificationCodeRepo.findByEmail(username);
        if(verificationCode==null || !verificationCode.getOtp().equals(otp)) {
            throw new BadCredentialsException("Invalid OTP");
        }
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }
}
