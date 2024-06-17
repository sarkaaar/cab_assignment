package com.abdullah.coding.challenge.Repositories;

import com.abdullah.coding.challenge.entities.Customer;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends CrudRepository<Customer, Integer> {
    Optional<Customer> findById(Integer id);

}
