package com.abdullah.coding.challenge.Repositories;

import com.abdullah.coding.challenge.entities.Booking;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingRepository extends CrudRepository<Booking, Integer> {

    @Query("SELECT b.id, b.bookingCode, b.creationTime, b.status, b.vehicalType, b.cab.id FROM Booking b INNER JOIN Customer c ON b.customer.id  = c.id WHERE b.creationTime > :currentDateTime AND c.id = :id ORDER BY b.creationTime ASC")
    List<Booking> findAllWithCreationTimeInTheFuture(@Param("currentDateTime") LocalDateTime currentDateTime, @Param("id") Integer id);

    @Query("SELECT b.id, b.bookingCode, b.creationTime, b.status, b.vehicalType, b.cab.id FROM Booking b INNER JOIN Customer c ON b.customer.id  = c.id WHERE b.creationTime < :currentDateTime AND c.id = :id ORDER BY b.creationTime ASC")
    List<Booking> findAllWithCreationTimeInThePast(@Param("currentDateTime") LocalDateTime currentDateTime, @Param("id") Integer id);

    Booking findByBookingCode(String bookingCode);


}
