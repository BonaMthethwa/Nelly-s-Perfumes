package com.perfumes.perfumeswebapp.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.NotNull;

@Document(collection = "product")
public class Product {
    @Id
    private String id;

    @NotNull
    private String productName;

    @NotNull
    private String description;

    @NotNull
    private double price;

    @NotNull
    private String image;

    @NotNull
    private String category;

    public Product(String productName, String description, double price, String image, String category) {
        this.productName = productName;
        this.description = description;
        this.price = price;
        this.image = image;
        this.category = category;
    }

    public Product() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCategoty() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
