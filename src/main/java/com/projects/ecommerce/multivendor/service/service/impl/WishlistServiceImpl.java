package com.projects.ecommerce.multivendor.service.service.impl;


import com.projects.ecommerce.multivendor.model.Product;
import com.projects.ecommerce.multivendor.model.User;
import com.projects.ecommerce.multivendor.model.Wishlist;
import com.projects.ecommerce.multivendor.repo.WishlistRepo;
import com.projects.ecommerce.multivendor.service.WishlistService;
import org.springframework.stereotype.Service;

@Service
public class WishlistServiceImpl implements WishlistService {
    private final WishlistRepo wishlistRepo;

    public WishlistServiceImpl(WishlistRepo wishlistRepo) {
        this.wishlistRepo = wishlistRepo;
    }

    @Override
    public Wishlist createWishlist(User user) {
    Wishlist wishlist = new Wishlist();
    wishlist.setUser(user);
    return wishlistRepo.save(wishlist);
    }

    @Override
    public Wishlist getWishlistByUserId(User user) {
        Wishlist wishlist =  wishlistRepo.findByUserId(user.getId());
        if (wishlist == null) {
            wishlist = createWishlist(user);
        }
        return wishlist;
    }

    @Override
    public Wishlist addProductToWishlist(User user, Product product) {
      Wishlist wishlist = getWishlistByUserId(user);

      if(wishlist.getProducts().contains(product)) {
          wishlist.getProducts().remove(product);
      }
      else{
          wishlist.getProducts().add(product);
      }
        return wishlistRepo.save(wishlist);
    }
}
