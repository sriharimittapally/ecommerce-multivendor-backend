package com.projects.ecommerce.multivendor.service;

import com.projects.ecommerce.multivendor.model.Home;
import com.projects.ecommerce.multivendor.model.HomeCategory;

import java.util.List;

public interface HomeService {
    public Home createHomePageData(List<HomeCategory> allCategories);
}
