package com.projects.ecommerce.multivendor.service;

import com.projects.ecommerce.multivendor.model.Seller;
import com.projects.ecommerce.multivendor.model.SellerReport;

public interface SellerReportService {
    SellerReport getSellerReport(Seller seller);
    SellerReport updateSellerReport(SellerReport sellerReport);
}
