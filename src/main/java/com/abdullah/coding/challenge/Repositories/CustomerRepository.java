package com.abdullah.coding.challenge.Repositories;

import com.abdullah.coding.challenge.entities.Customer;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends CrudRepository<Customer, Integer> {
    Customer findById(String id);

}
