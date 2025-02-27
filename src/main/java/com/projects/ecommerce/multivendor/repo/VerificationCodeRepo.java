package com.projects.ecommerce.multivendor.repo;

import com.projects.ecommerce.multivendor.model.User;
import com.projects.ecommerce.multivendor.model.VerificationCode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VerificationCodeRepo extends JpaRepository<VerificationCode, Long> {
    VerificationCode findByEmail(String email);
    VerificationCode findByOtp(String otp);

}
