package com.perfumes.perfumeswebapp.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "order_item")
public class OrderItem {
    @Id
    private String id;
    private String productName;
    private double price;

    private String image;
    private Integer quantity;

    public OrderItem() {
    }

    public OrderItem(String productName, double price, String image, Integer quantity) {
        this.productName = productName;
        this.price = price;
        this.image = image;
        this.quantity = quantity;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductName() {
        return productName;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Double getPrice() {
        return price;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getImage() {
        return image;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getQuantity() {
        return quantity;
    }
}
