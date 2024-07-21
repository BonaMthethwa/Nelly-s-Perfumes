package com.perfumes.perfumeswebapp.model;

import java.util.*;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "cartItems")
public class Cart {
    @Id
    private String id;
    @DBRef(lazy = true)
    private User user;
    private Product product;
    private List<CartItem> items = new ArrayList<>();
    private double totalPrice;

    public Cart() {

    }

    public Cart(User user, Product product, List<CartItem> items, double totalPrice) {
        this.user = user;
        this.product = product;
        this.items = items;
        this.totalPrice = totalPrice;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Product getProduct() {
        return product;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void setItems(List<CartItem> items) {
        this.items = items;
    }

    public List<CartItem> getItems() {
        return items;
    }

    public void setTotalPrice(double totalprice) {
        this.totalPrice = totalprice;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void clearCart() {
        items.clear();
    }
}