package com.abdullah.coding.challenge.Repositories;

import com.abdullah.coding.challenge.entities.Cab;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CabRepository extends CrudRepository<Cab, Integer> {

}
