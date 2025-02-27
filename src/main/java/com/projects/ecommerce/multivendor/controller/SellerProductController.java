package com.projects.ecommerce.multivendor.controller;

import com.projects.ecommerce.multivendor.exceptions.ProductException;
import com.projects.ecommerce.multivendor.exceptions.SellerException;
import com.projects.ecommerce.multivendor.model.Product;
import com.projects.ecommerce.multivendor.model.Seller;
import com.projects.ecommerce.multivendor.request.CreateProductRequest;
import com.projects.ecommerce.multivendor.service.ProductService;
import com.projects.ecommerce.multivendor.service.SellerService;
import jdk.jshell.spi.ExecutionControl;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/sellers/products")
public class SellerProductController {
    private final ProductService productService;
    private final SellerService sellerService;

    public SellerProductController(ProductService productService, SellerService sellerService) {
        this.productService = productService;
        this.sellerService = sellerService;
    }

    @GetMapping
    public ResponseEntity<List<Product>> getProductBySellerId(@RequestHeader("Authorization") String jwt) throws Exception {
        Seller seller = sellerService.getSellerProfile(jwt);
        List<Product> products = productService.getProductsBySellerId(seller.getId());
        return new ResponseEntity<>(products, HttpStatus.OK);
    }


    @PostMapping
    public ResponseEntity<Product> createProduct(
            @RequestBody CreateProductRequest request,
            @RequestHeader("Authorization") String jwt) throws Exception {

        Seller seller = sellerService.getSellerProfile(jwt);

        Product product = productService.createProduct(request, seller);
        return new ResponseEntity<>(product, HttpStatus.CREATED);
    }


    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long productId) {
        try{
            productService.deleteProduct(productId);
            return new ResponseEntity<>(HttpStatus.OK);
        }catch (ProductException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @PutMapping("/{productId}")
    public ResponseEntity<Product>  updateProduct(@PathVariable Long productId,
                                                  @RequestBody Product product) throws ProductException {
        Product updatedProduct = productService.updateProduct(productId, product);
        return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
    }
}



