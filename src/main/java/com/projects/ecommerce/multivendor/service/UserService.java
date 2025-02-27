package com.projects.ecommerce.multivendor.service;

import com.projects.ecommerce.multivendor.model.Address;
import com.projects.ecommerce.multivendor.model.User;

public interface UserService {
     User findUserByJwtToken(String jwt) throws Exception;
     User findUserByEmail(String email) throws Exception;
     Address saveAddress(User user,Address address) throws Exception;
}
