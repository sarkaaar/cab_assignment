package com.abdullah.coding.challenge.Repositories;

import com.abdullah.coding.challenge.entities.Cancellation;
import org.springframework.data.repository.CrudRepository;

public interface CancellationRepository extends CrudRepository<Cancellation, Integer> {
    Cancellation findByBookingCode(String code);
}
