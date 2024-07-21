package com.perfumes.perfumeswebapp.Repositories;

import com.perfumes.perfumeswebapp.model.Cart;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CartRepository extends MongoRepository<Cart, String> {
    Cart findByUserId(String id);
}
