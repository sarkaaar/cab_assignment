package com.abdullah.coding.challenge.Repositories;

import com.abdullah.coding.challenge.entities.Booking;
import com.abdullah.coding.challenge.entities.Rating;
import org.springframework.data.repository.CrudRepository;

public interface RatingRepository extends CrudRepository<Rating, Integer> {

}
