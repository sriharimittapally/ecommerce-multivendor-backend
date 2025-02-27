package com.projects.ecommerce.multivendor.service.service.impl;

import com.projects.ecommerce.multivendor.model.Seller;
import com.projects.ecommerce.multivendor.model.SellerReport;
import com.projects.ecommerce.multivendor.repo.SellerReportRepo;
import com.projects.ecommerce.multivendor.service.SellerReportService;
import org.springframework.stereotype.Service;

@Service
public class SellerReportServiceImpl implements SellerReportService {

    public final SellerReportRepo sellerReportRepo;
    public SellerReportServiceImpl(SellerReportRepo sellerReportRepo) {
        this.sellerReportRepo = sellerReportRepo;
    }

    @Override
    public SellerReport getSellerReport(Seller seller) {

    SellerReport sr = sellerReportRepo.findBySellerId(seller.getId());
    if (sr == null) {
        SellerReport newReport = new SellerReport();
        newReport.setSeller(seller);
        return sellerReportRepo.save(newReport);
    }
        return sr;
    }

    @Override
    public SellerReport updateSellerReport(SellerReport sellerReport) {
        return sellerReportRepo.save(sellerReport);
    }
}
