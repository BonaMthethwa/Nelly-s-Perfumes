package com.perfumes.perfumeswebapp.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.NotNull;

@Document(collection = "address")
public class Address {
    @Id
    private String id;
    @NotNull
    private String street;
    @NotNull
    private String city;
    @NotNull
    private String code;
    @NotNull
    private String country;

    @DBRef(lazy = true)
    private User user;

    @DBRef(lazy = true)
    private Order order;

    public Address() {
    }

    public Address(String street, String city, String code, String country, User user, Order order) {
        this.street = street;
        this.city = city;
        this.code = code;
        this.country = country;
        this.user = user;
        this.order = order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Order getOrder() {
        return order;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getStreet() {
        return street;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCity() {
        return city;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCountry() {
        return country;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

}
