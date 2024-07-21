package com.perfumes.perfumeswebapp.Repositories;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.perfumes.perfumeswebapp.model.Order;
import com.perfumes.perfumeswebapp.model.User;

import java.util.List;

public interface OrderRepository extends MongoRepository<Order, String> {

    List<Order> findByUser(User user);
}
