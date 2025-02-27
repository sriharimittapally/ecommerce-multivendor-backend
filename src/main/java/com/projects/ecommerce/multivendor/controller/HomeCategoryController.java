package com.projects.ecommerce.multivendor.controller;

import com.projects.ecommerce.multivendor.model.Home;
import com.projects.ecommerce.multivendor.model.HomeCategory;
import com.projects.ecommerce.multivendor.service.HomeCategoryService;
import com.projects.ecommerce.multivendor.service.HomeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class HomeCategoryController {
    private final HomeCategoryService homeCategoryService;
    private final HomeService homeService;

    public HomeCategoryController(HomeCategoryService homeCategoryService, HomeService homeService) {
        this.homeCategoryService = homeCategoryService;
        this.homeService = homeService;
    }



    @PostMapping("/home/categories")
    public ResponseEntity<Home> createHomeCategories(
            @RequestBody List<HomeCategory> homeCategories
            ){
    List<HomeCategory> categories = homeCategoryService.createCategories(homeCategories);
    Home home = homeService.createHomePageData(categories);
    return new ResponseEntity<>(home, HttpStatus.ACCEPTED);
    }





    @GetMapping("/admin/home-category")
    public ResponseEntity<List<HomeCategory>> getHomeCategory() throws Exception{
        List<HomeCategory>  categories = homeCategoryService.getAllHomeCategories();
        return ResponseEntity.ok(categories);
    }





    @PatchMapping("/admin/home-category/{id}")
    public ResponseEntity<HomeCategory> updateHomeCategory(
            @PathVariable Long id,
            @RequestBody HomeCategory homeCategory
    )throws Exception{
        HomeCategory updatedCategory = homeCategoryService.updateHomeCategory(homeCategory, id);
        return ResponseEntity.ok(updatedCategory);
    }
}
