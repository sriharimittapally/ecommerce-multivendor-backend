package com.projects.ecommerce.multivendor.controller;

import com.projects.ecommerce.multivendor.model.Deal;
import com.projects.ecommerce.multivendor.response.ApiResponse;
import com.projects.ecommerce.multivendor.service.DealService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/deals")
public class DealController {
    public final DealService dealService;


    public DealController(DealService dealService) {
        this.dealService = dealService;
    }


    @GetMapping
    public ResponseEntity <List<Deal>> getAllDeals() {
         List<Deal> allDeals = dealService.getAllDeals();
        return new ResponseEntity<>(allDeals, HttpStatus.ACCEPTED);
    }

    @PostMapping
    public ResponseEntity<Deal> createDeal(@RequestBody Deal deals) {
        Deal createdDeal = dealService.createDeal(deals);
        return new ResponseEntity<>(createdDeal, HttpStatus.ACCEPTED);
    }


    @PatchMapping("/{id}")
    public ResponseEntity<Deal> updateDeal(@PathVariable Long id, @RequestBody Deal deal) throws Exception{
        Deal updatedDeal = dealService.updateDeal(deal, id);
        return ResponseEntity.ok(updatedDeal);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteDeal(@PathVariable Long id) throws Exception {

        dealService.deleteDeal(id);

        ApiResponse res = new ApiResponse();
        res.setMessage("Deal deleted successfully");

        return new ResponseEntity<>(res, HttpStatus.ACCEPTED);
    }
}
