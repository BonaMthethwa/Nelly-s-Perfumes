package com.perfumes.perfumeswebapp.Services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.perfumes.perfumeswebapp.model.Product;
import com.perfumes.perfumeswebapp.Repositories.ProductRepository;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public void addProduct(String productName, String description, double price, String imageName, String category) {

        Product product = new Product(productName, description, price, imageName, category);

        productRepository.save(product);
    }

    public void deleteProductByName(String productName) {
        productRepository.deleteByProductName(productName);
    }

    public Optional<Product> findByProductName(String productName) {
        return productRepository.findByProductName(productName);

    }

    public List<Product> getProductsByCategory(String category) {
        return productRepository.findByCategory(category);
    }

}
