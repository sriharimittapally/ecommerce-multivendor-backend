package com.projects.ecommerce.multivendor.service;

import com.projects.ecommerce.multivendor.exceptions.ProductException;
import com.projects.ecommerce.multivendor.model.Product;
import com.projects.ecommerce.multivendor.model.Seller;
import com.projects.ecommerce.multivendor.request.CreateProductRequest;
import org.springframework.data.domain.Page;


import java.util.List;

public interface ProductService {
    public Product createProduct(CreateProductRequest req, Seller seller);
    public void deleteProduct(Long productId) throws ProductException;
    public Product updateProduct(Long productId, Product product) throws ProductException;
    Product findProductById(Long productId) throws ProductException;
    List<Product> searchProducts(String query);
    public Page<Product> getAllProducts(
    String category,
    String brand,
    String colors,
    String sizes,
    Integer minPrice,
    Integer maxPrice,
    Integer minDiscount,
    String sort,
    String stock,
    Integer pageNumber
    );

    List<Product> getProductsBySellerId(Long sellerId);
}
