package com.projects.ecommerce.multivendor.controller;



import com.projects.ecommerce.multivendor.response.ApiResponse;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("/")
    public ApiResponse HomeControllerHandler(){
      ApiResponse apiResponse = new ApiResponse();
      apiResponse.setMessage("welcome to ecommere multi vendor");
      return apiResponse;
    }
}
