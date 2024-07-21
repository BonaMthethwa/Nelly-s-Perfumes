package com.perfumes.perfumeswebapp.Repositories;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.perfumes.perfumeswebapp.model.Address;

public interface AddressRepository extends MongoRepository<Address, String> {

}
