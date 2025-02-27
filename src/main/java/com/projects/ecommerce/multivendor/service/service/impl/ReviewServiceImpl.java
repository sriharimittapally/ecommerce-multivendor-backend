package com.projects.ecommerce.multivendor.service.service.impl;

import com.projects.ecommerce.multivendor.model.Product;
import com.projects.ecommerce.multivendor.model.Review;
import com.projects.ecommerce.multivendor.model.User;
import com.projects.ecommerce.multivendor.repo.ReviewRepo;
import com.projects.ecommerce.multivendor.request.CreateReviewRequest;
import com.projects.ecommerce.multivendor.service.ReviewService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepo reviewRepo;

    public ReviewServiceImpl(ReviewRepo reviewRepo) {
        this.reviewRepo = reviewRepo;
    }

    @Override
    public Review createReview(CreateReviewRequest req, User user, Product product) {
        Review review = new Review();
        review.setUser(user);
        review.setProduct(product);
        review.setReviewText(req.getReviewText());
        review.setRating(req.getReviewRating());
        review.setProductImages(req.getProductImages());

        product.getReviews().add(review);
        return reviewRepo.save(review);
    }

    @Override
    public List<Review> getReviewByProductId(Long productId) {
        return reviewRepo.findByProductId(productId);
    }

    @Override
    public Review updateReview(Long reviewId, String reviewText, double rating, Long userId) throws Exception {
        Review review = getReviewById(reviewId);
        if(review.getUser().getId().equals(userId)) {
            review.setReviewText(reviewText);
            review.setRating(rating);
            return reviewRepo.save(review);
        }
        throw new Exception("You can't update this Review");
    }

    @Override
    public Review getReviewById(Long reviewId) throws Exception {
        return reviewRepo.findById(reviewId).orElseThrow(()->new Exception("Review not found"));
    }

    @Override
    public void deleteReview(Long reviewId, Long userId) throws Exception {
        Review review = getReviewById(reviewId);
        if(review.getUser().getId().equals(userId)) {
            throw new Exception("you can't delete this Review");
        }
        reviewRepo.delete(review);
    }




}
