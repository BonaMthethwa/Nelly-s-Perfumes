package com.perfumes.perfumeswebapp.Repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.perfumes.perfumeswebapp.model.Product;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends MongoRepository<Product, String> {

    Optional<Product> findByProductName(String productName);

    public void deleteByProductName(String productName);

    public List<Product> findByCategory(String category);
}
