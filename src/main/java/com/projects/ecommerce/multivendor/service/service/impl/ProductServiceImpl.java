package com.projects.ecommerce.multivendor.service.service.impl;

import com.projects.ecommerce.multivendor.exceptions.ProductException;
import com.projects.ecommerce.multivendor.model.Category;
import com.projects.ecommerce.multivendor.model.Product;
import com.projects.ecommerce.multivendor.model.Seller;
import com.projects.ecommerce.multivendor.repo.CategoryRepo;
import com.projects.ecommerce.multivendor.repo.ProductRepo;
import com.projects.ecommerce.multivendor.request.CreateProductRequest;
import com.projects.ecommerce.multivendor.service.ProductService;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepo productRepo;
    private final CategoryRepo categoryRepo;

    public ProductServiceImpl(ProductRepo productRepo, CategoryRepo categoryRepo) {
        this.productRepo = productRepo;
        this.categoryRepo = categoryRepo;
    }

    @Override
    public Product createProduct(CreateProductRequest req, Seller seller) {
        Category category1 = categoryRepo.findByCategoryId(req.getCategory());

        if(category1 == null) {
            Category category = new Category();
            category.setCategoryId(req.getCategory());
            category.setLevel(1);
            category1 = categoryRepo.save(category);
        }

        Category category2 = categoryRepo.findByCategoryId(req.getCategory2());
        if(category2 == null) {
            Category category = new Category();
            category.setCategoryId(req.getCategory2());
            category.setLevel(2);
            category.setParentCategory(category1);
            category2 = categoryRepo.save(category);

        }

        Category category3 = categoryRepo.findByCategoryId(req.getCategory3());
        if(category3 == null) {
            Category category = new Category();
            category.setCategoryId(req.getCategory3());
            category.setLevel(3);
            category.setParentCategory(category2);
            category3 = categoryRepo.save(category);
        }

        int discountPercentage = calculateDiscountPercentage(req.getMrpPrice(), req.getSellingPrice());


        Product product = new Product();
        product.setSeller(seller);
        product.setCategory(category3);
        product.setDescription(req.getDescription());
        product.setCreatedAt(LocalDateTime.now());
        product.setTitle(req.getTitle());
        product.setColor(req.getColor());
        product.setSellingPrice(req.getSellingPrice());
        product.setImages(req.getImages());
        product.setMrpPrice(req.getMrpPrice());
        product.setSizes(req.getSizes());
        product.setDiscountPercent(discountPercentage);
        return productRepo.save(product);
    }
    private int calculateDiscountPercentage(int mrpPrice, int sellingPrice) {
        if(mrpPrice <= sellingPrice) {
            throw new IllegalArgumentException("Actual price must be greater O");
        }
        double discount = mrpPrice-sellingPrice;
        double discountPercentage = (discount / mrpPrice)*100;
        return (int)discountPercentage;
    }


    @Override
    public void deleteProduct(Long productId) throws ProductException {

        Product product = findProductById(productId);
        productRepo.delete(product);
    }

    @Override
    public Product updateProduct(Long productId, Product product) throws ProductException {
        findProductById(productId);
        product.setId(productId);
        return productRepo.save(product);
    }

    @Override
    public Product findProductById(Long productId) throws ProductException {
        return productRepo.findById(productId).orElseThrow(()->
                new ProductException("Product not found with id "+productId));
    }

    @Override
    public List<Product> searchProducts(String query) {
        return productRepo.searchProduct(query);
    }


    @Override
    public Page<Product> getAllProducts(String category, String brand, String colors, String sizes, Integer minPrice, Integer maxPrice, Integer minDiscount, String sort, String stock, Integer pageNumber) {
        Specification<Product> spec = (root, query, criteriaBuilder) ->{
            List<Predicate> predicates = new ArrayList<>();
            if(category != null) {
                Join<Product, Category> categoryJoin = root.join("category");
                predicates.add(criteriaBuilder.equal(categoryJoin.get("categoryId"), category));
            }
            if(brand != null && !brand.isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("brand"), brand));
            }
            if(colors != null && !colors.isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("color"), colors));
            }
            if(sizes != null && !sizes.isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("size"), sizes));
            }
            if(minPrice != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("sellingPrice"), minPrice));
            }
            if(maxPrice != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("sellingPrice"), maxPrice));
            }
            if(minDiscount != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("discountPercent"), minDiscount));
            }
            if(stock != null) {
                predicates.add(criteriaBuilder.equal(root.get("stock"), stock));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
        Pageable pageable;
        if(sort != null && !sort.isEmpty()) {
            pageable = switch (sort) {
                case "price_low" -> PageRequest.of(pageNumber != null ? pageNumber : 0, 10,
                        Sort.by("sellingPrice").ascending());
                case "price_high" -> PageRequest.of(pageNumber != null ? pageNumber : 0, 10,
                        Sort.by("sellingPrice").descending());
                default -> PageRequest.of(pageNumber != null ? pageNumber : 0, 10,
                        Sort.unsorted());
            };
        }
        else{
            pageable = PageRequest.of(pageNumber != null ? pageNumber : 0, 10,
                    Sort.unsorted());
        }
        return productRepo.findAll(spec,pageable);

    }

    @Override
    public List<Product> getProductsBySellerId(Long sellerId) {
        return productRepo.findBySellerId(sellerId);
    }
}
