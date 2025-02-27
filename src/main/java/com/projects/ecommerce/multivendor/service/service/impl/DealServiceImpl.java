package com.projects.ecommerce.multivendor.service.service.impl;

import com.projects.ecommerce.multivendor.model.Deal;
import com.projects.ecommerce.multivendor.model.HomeCategory;
import com.projects.ecommerce.multivendor.repo.DealRepo;
import com.projects.ecommerce.multivendor.repo.HomeCategoryRepo;
import com.projects.ecommerce.multivendor.service.DealService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DealServiceImpl implements DealService {

    public final DealRepo dealRepo;
    public final HomeCategoryRepo homeCategoryRepo;

    public DealServiceImpl(DealRepo dealRepo, HomeCategoryRepo homeCategoryRepo) {
        this.dealRepo = dealRepo;
        this.homeCategoryRepo = homeCategoryRepo;
    }

    @Override
    public List<Deal> getAllDeals() {
        return dealRepo.findAll();
    }

    @Override
    public Deal createDeal(Deal deal) {
        HomeCategory category = homeCategoryRepo.findById(deal.getCategory().getId()).orElse(null);
        Deal newDeal = dealRepo.save(deal);
        newDeal.setCategory(category);
        newDeal.setDiscount(deal.getDiscount());
        return dealRepo.save(newDeal);
    }

    @Override
    public Deal updateDeal(Deal deal, Long id) throws  Exception{
        Deal existingDeal = dealRepo.findById(deal.getId()).orElse(null);
        HomeCategory category = homeCategoryRepo.findById(deal.getCategory().getId()).orElse(null);

        if(existingDeal != null) {
            if(deal.getDiscount() != null) {
                existingDeal.setDiscount(deal.getDiscount());
            }
            if(category != null) {
                existingDeal.setCategory(category);
            }
            return dealRepo.save(existingDeal);
        }
        throw  new Exception("Deal not found..!");
    }



    @Override
    public void deleteDeal(Long id) throws Exception {
        Deal deal = dealRepo.findById(id).orElseThrow(()->
                new Exception("Deal not found"));
        dealRepo.delete(deal);
    }
}
