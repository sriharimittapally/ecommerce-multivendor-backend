package com.projects.ecommerce.multivendor.controller;

import com.projects.ecommerce.multivendor.config.JwtProvider;
import com.projects.ecommerce.multivendor.domain.AccountStatus;
import com.projects.ecommerce.multivendor.exceptions.SellerException;
import com.projects.ecommerce.multivendor.model.Seller;
import com.projects.ecommerce.multivendor.model.SellerReport;
import com.projects.ecommerce.multivendor.model.VerificationCode;
import com.projects.ecommerce.multivendor.repo.VerificationCodeRepo;
import com.projects.ecommerce.multivendor.request.LoginRequest;
import com.projects.ecommerce.multivendor.response.ApiResponse;
import com.projects.ecommerce.multivendor.response.AuthResponse;
import com.projects.ecommerce.multivendor.service.AuthService;
import com.projects.ecommerce.multivendor.service.EmailService;
import com.projects.ecommerce.multivendor.service.SellerReportService;
import com.projects.ecommerce.multivendor.service.SellerService;
import com.projects.ecommerce.multivendor.util.OtpUtil;
import jakarta.mail.MessagingException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sellers")
public class SellerController {
    private final SellerService sellerService;
    private final VerificationCodeRepo verificationCodeRepo;
    private final AuthService authService;
    private final EmailService emailService;
    private final JwtProvider jwtProvider;
    private final SellerReportService sellerReportService;

    public SellerController(SellerService sellerService, VerificationCodeRepo verificationCodeRepo, AuthService authService, EmailService emailService, JwtProvider jwtProvider, SellerReportService sellerReportService) {
        this.sellerService = sellerService;
        this.verificationCodeRepo = verificationCodeRepo;
        this.authService = authService;
        this.emailService = emailService;
        this.jwtProvider = jwtProvider;
        this.sellerReportService = sellerReportService;
    }


        @PostMapping("/login")
        public ResponseEntity<AuthResponse> loginSeller(@RequestBody LoginRequest req) throws Exception {
                String otp = req.getOtp();
                String email = req.getEmail();

            req.setEmail("seller_"+email);
            AuthResponse authResponse = authService.signing(req);
                return ResponseEntity.ok(authResponse);
        }


        @PatchMapping("/verify/{otp}")
        public ResponseEntity<Seller> verifySellerEmail(@PathVariable String otp) throws Exception {
            VerificationCode verificationCode = verificationCodeRepo.findByOtp(otp);
            if(verificationCode == null || !verificationCode.getOtp().equals(otp)) {
                throw new Exception("Invalid OTP!!");
            }
            Seller seller = sellerService.verifyEmail(verificationCode.getEmail(), otp);

            return new ResponseEntity<>(seller, HttpStatus.OK);
        }




        @PostMapping()
        public ResponseEntity<Seller> createSeller(@RequestBody Seller seller) throws Exception, MessagingException {
            System.out.println("Seller:"+ seller);
            Seller savedSeller = sellerService.createSeller(seller);
        
            // Generate and store OTP
            String otp = OtpUtil.generateOtp();
            VerificationCode verificationCode = new VerificationCode();
            verificationCode.setOtp(otp);
            verificationCode.setEmail(seller.getEmail());
            verificationCodeRepo.save(verificationCode);
        
            
            // Prepare professional email content
            String subject = "Verify Your Account – ShopEasy";
            String frontendUrl = "http://localhost:5173/verify-seller/" + verificationCode.getOtp();
            String text = "<html><body style='font-family: Arial, sans-serif; background-color: #f4f4f4; padding: 5px; margin: 0;'>"
            + "<table role='presentation' width='100%' cellpadding='0' cellspacing='0' border='0' style='background: #f4f4f4;'>"
            + "<tr><td align='center' style='padding: 10px;'>"
            + "<table role='presentation' width='100%' cellpadding='0' cellspacing='0' border='0' style='background: white; width: 100%; max-width: 800px; padding: 20px; border-radius: 8px; box-shadow: 0 0 10px rgba(0, 0, 0, 0.1); table-layout: fixed;'>"
            + "<tr><td align='center' style='padding: 10px 0;'><h2 style='color:rgb(5, 155, 255); margin: 0;'>Welcome to ShopEasy, " + seller.getSellerName() + "!</h2></td></tr>"
            + "<tr><td align='center' style='padding: 10px 0;'><p style='font-size: 16px; color: #333; margin: 0;'>Thank you for registering as a seller with us.</p></td></tr>"
            + "<tr><td align='center' style='padding: 10px 0;'><p style='font-size: 18px; font-weight: bold; color: #444; margin: 0;'>Verification Code:</p></td></tr>"
            + "<tr><td align='center' style='padding: 10px 0;'><div style='font-size: 24px; font-weight: bold; color: #fff; background:#717171; padding: 10px 20px; border-radius: 5px; display: inline-block;'>" + verificationCode.getOtp() + "</div></td></tr>"
            + "<tr><td align='center' style='padding: 10px 20px;'><p style='font-size: 16px; color: #333; margin: 0;'>Click the button below to verify your email:</p></td></tr>"
            + "<tr><td align='center' style='padding: 10px 0;'><a href='" + frontendUrl + "' target='_blank' style='background:rgb(35, 136, 252); color: white; padding: 12px 24px; font-size: 16px; font-weight: bold; text-decoration: none; border-radius: 5px; display: inline-block;'>Verify Your Email</a></td></tr>"
            + "<tr><td align='center' style='padding: 20px 0;'><p style='font-size: 14px; color: #777; margin: 0;'>If you did not request this, please ignore this email.</p><p style='font-size: 14px; color: #555; margin: 5px 0;'><strong>The ShopEasy Seller Team</strong></p></td></tr>"
            + "</table></td></tr></table></body></html>";
    
        
            emailService.sendVerificationOtpEmail(seller.getEmail(), verificationCode.getOtp(), subject, text);
        
            return new ResponseEntity<>(savedSeller, HttpStatus.CREATED);
        }
        
        




        @GetMapping("/{id}")
        public ResponseEntity<Seller> getSellerById(@PathVariable Long id) throws SellerException {
            Seller seller = sellerService.getSellerById(id);
            return new ResponseEntity<>(seller, HttpStatus.OK);
        }




        @GetMapping("/profile")
        public ResponseEntity<Seller> getSellerByJwt(
                @RequestHeader("Authorization") String jwt ) throws Exception
        {
            Seller seller = sellerService.getSellerProfile(jwt);
            return new ResponseEntity<>(seller, HttpStatus.OK);
        }




        @GetMapping("/report")
        public ResponseEntity<SellerReport> getSellerReport(
                @RequestHeader("Authorization") String jwt) throws Exception{
                    Seller seller = sellerService.getSellerProfile(jwt);
                    SellerReport report = sellerReportService.getSellerReport(seller);
                    return new ResponseEntity<>(report, HttpStatus.OK);
        }





        @GetMapping
        public ResponseEntity<List> getAllSellers(
                @RequestParam (required = false) AccountStatus status

                ){
                List<Seller> sellers = sellerService.getAllSellers(status);
                return ResponseEntity.ok(sellers);
        }





        @PatchMapping()
        public ResponseEntity<Seller> updateSeller(
                @RequestHeader("Authorization") String jwt,
                @RequestBody Seller seller) throws Exception{

            Seller profile = sellerService.getSellerProfile(jwt);
            Seller updatedSeller = sellerService.updateSeller(profile.getId(), seller);
            return ResponseEntity.ok(updatedSeller);
        }



        @DeleteMapping("/{id}")
        public ResponseEntity<Void> deleteSeller(@PathVariable Long id) throws Exception{
        sellerService.deleteSeller(id);
        return ResponseEntity.noContent().build();
        }
}



