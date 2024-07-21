package com.perfumes.perfumeswebapp.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.perfumes.perfumeswebapp.Repositories.AddressRepository;
import com.perfumes.perfumeswebapp.model.Address;
import com.perfumes.perfumeswebapp.model.Order;
import com.perfumes.perfumeswebapp.model.User;

import jakarta.servlet.http.HttpSession;

import java.util.List;
import java.util.Optional;

@Service
public class AddressService {

    @Autowired
    private final AddressRepository addressRepository;

    public AddressService(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    public List<Address> getAllAddresses() {
        return addressRepository.findAll();
    }

    public Optional<Address> getAddressById(String id) {
        return addressRepository.findById(id);
    }

    public Address saveAddress(String street, String city, String code, String country, User user, Order order,
            HttpSession session) {
        session.setAttribute("user", user);
        Address address = new Address(street, city, code, country, user, order);
        return addressRepository.save(address);
    }

    public void deleteAddressById(String id) {
        addressRepository.deleteById(id);
    }
}
