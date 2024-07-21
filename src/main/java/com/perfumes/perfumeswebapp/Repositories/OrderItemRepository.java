package com.perfumes.perfumeswebapp.Repositories;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.perfumes.perfumeswebapp.model.OrderItem;

public interface OrderItemRepository extends MongoRepository<OrderItem, String> {

}
