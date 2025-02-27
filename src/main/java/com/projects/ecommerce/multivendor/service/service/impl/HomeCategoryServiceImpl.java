package com.projects.ecommerce.multivendor.service.service.impl;

import com.projects.ecommerce.multivendor.model.HomeCategory;
import com.projects.ecommerce.multivendor.repo.HomeCategoryRepo;
import com.projects.ecommerce.multivendor.service.HomeCategoryService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HomeCategoryServiceImpl implements HomeCategoryService {

    private final HomeCategoryRepo homeCategoryRepo;

    public HomeCategoryServiceImpl(HomeCategoryRepo homeCategoryRepo) {
        this.homeCategoryRepo = homeCategoryRepo;
    }

    @Override
    public HomeCategory createHomeCategory(HomeCategory homeCategory) {
        return homeCategoryRepo.save(homeCategory);
    }

    @Override
    public List<HomeCategory> createCategories(List<HomeCategory> homeCategories) {
        if(homeCategoryRepo.findAll().isEmpty()) {
            return homeCategoryRepo.saveAll(homeCategories);
        }
        return homeCategoryRepo.findAll();

    }

    @Override
    public HomeCategory updateHomeCategory(HomeCategory category, Long id) throws Exception {
        HomeCategory existingHomeCategory = homeCategoryRepo.findById(id)
                .orElseThrow(()-> new Exception("HomeCategory not found"));
        if(category.getImage()!= null){
            existingHomeCategory.setImage(category.getImage());
        }
        if(category.getCategoryId()!= null){
            existingHomeCategory.setCategoryId(category.getCategoryId());
        }
        return homeCategoryRepo.save(existingHomeCategory);
    }

    @Override
    public List<HomeCategory> getAllHomeCategories() {
         return homeCategoryRepo.findAll();
    }
}
