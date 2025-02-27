package com.projects.ecommerce.multivendor.service;

import com.projects.ecommerce.multivendor.model.Deal;

import java.util.List;

public interface DealService {
    List<Deal> getAllDeals();
    Deal createDeal(Deal deal);
    Deal updateDeal(Deal deal, Long id) throws Exception;
    void deleteDeal(Long id) throws Exception;
}
