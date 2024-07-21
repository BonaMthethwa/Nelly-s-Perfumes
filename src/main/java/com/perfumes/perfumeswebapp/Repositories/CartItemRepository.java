package com.perfumes.perfumeswebapp.Repositories;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.perfumes.perfumeswebapp.model.CartItem;

public interface CartItemRepository extends MongoRepository<CartItem, String> {

}
