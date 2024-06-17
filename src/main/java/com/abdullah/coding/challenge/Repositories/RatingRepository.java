package com.abdullah.coding.challenge.Repositories;

import com.abdullah.coding.challenge.entities.Booking;
import com.abdullah.coding.challenge.entities.Rating;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RatingRepository extends CrudRepository<Rating, Integer> {

    @Query("SELECT r.rating FROM Rating r WHERE r.cabId = :cabId")
    List<Integer> findRatingsByCabId(@Param("cabId") Integer cabId);

}
