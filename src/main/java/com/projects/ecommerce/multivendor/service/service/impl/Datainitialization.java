package com.projects.ecommerce.multivendor.service.service.impl;

import com.projects.ecommerce.multivendor.domain.USER_ROLE;
import com.projects.ecommerce.multivendor.model.User;
import com.projects.ecommerce.multivendor.repo.UserRepo;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class Datainitialization implements CommandLineRunner {

    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;

    public Datainitialization(UserRepo userRepo, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    public void run(String... args){
        initializeAdminUser();
    }

    private void initializeAdminUser() {
        String adminUsername = "sriharimittapally95@gmail.com";

        if(userRepo.findByEmail(adminUsername) == null) {
            User adminUser = new User();
            adminUser.setPassword(passwordEncoder.encode("admin12345"));
            adminUser.setEmail(adminUsername);
            adminUser.setFullName("Admin Srihari");
            adminUser.setRole(USER_ROLE.ROLE_ADMIN);

            User admin = userRepo.save(adminUser);
        }
    }


}
