package com.projects.ecommerce.multivendor.service.service.impl;

import com.projects.ecommerce.multivendor.domain.HomeCategorySection;
import com.projects.ecommerce.multivendor.model.Deal;
import com.projects.ecommerce.multivendor.model.Home;
import com.projects.ecommerce.multivendor.model.HomeCategory;
import com.projects.ecommerce.multivendor.repo.DealRepo;
import com.projects.ecommerce.multivendor.service.HomeService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class HomeServiceImpl implements HomeService {

    private final DealRepo dealRepo;

    public HomeServiceImpl(DealRepo dealRepo) {
        this.dealRepo = dealRepo;
    }

    @Override
    public Home createHomePageData(List<HomeCategory> allCategories) {
    List<HomeCategory>  gridCategories  = allCategories.stream()
            .filter(category ->
                    category.getSection() == HomeCategorySection.GRID)
                            .collect(Collectors.toList());

    List<HomeCategory>  shopByCategories  = allCategories.stream()
            .filter(category ->
                    category.getSection() == HomeCategorySection.SHOP_BY_CATEGORIES)
                        .collect(Collectors.toList());

    List<HomeCategory>  electricCategories  = allCategories.stream()
            .filter(category ->
                    category.getSection() == HomeCategorySection.ELECTRIC_CATEGORIES)
                        .collect(Collectors.toList());
    List<HomeCategory>  dealsCategories  = allCategories.stream()
            .filter(category ->
                    category.getSection() == HomeCategorySection.DEALS)
                        .collect(Collectors.toList());

    List<Deal>  createdDeals = new ArrayList<>();

    if(dealRepo.findAll().isEmpty()){
        List<Deal> deals = allCategories.stream()
                .filter(category -> category.getSection() == HomeCategorySection.DEALS)
                .map(category -> new Deal(null, 10, category))
                .collect(Collectors.toList());
        createdDeals = dealRepo.saveAll(deals);
    }
    else{
        createdDeals = dealRepo.findAll();
    }

    Home home = new Home();
    home.setGrid(gridCategories);
    home.setShopByCategories(shopByCategories);
    home.setElectricCategories(electricCategories);
    home.setDeals(createdDeals);
    home.setDealCategories(dealsCategories);
    return home;
    }
}
