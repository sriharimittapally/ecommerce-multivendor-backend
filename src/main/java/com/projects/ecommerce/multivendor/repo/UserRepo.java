package com.projects.ecommerce.multivendor.repo;

import com.projects.ecommerce.multivendor.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User, Long> {

    User findByEmail(String email);

}
