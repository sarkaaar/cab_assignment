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

    Booking findByBookingCode(String bookingCode);

    @Query("SELECT b.id, b.bookingCode, b.creationTime, b.status, b.vehicalType, b.cab.id FROM Booking b INNER JOIN Customer c ON b.customer.id  = c.id " +
            "WHERE b.creationTime > :currentDateTime AND c.id = :id ORDER BY b.creationTime ASC")
    List<Booking> findAllWithCreationTimeInTheFuture(@Param("currentDateTime") LocalDateTime currentDateTime, @Param("id") Integer id);

    @Query("SELECT b.id, b.bookingCode, b.creationTime, b.status, b.vehicalType, b.cab.id FROM Booking b INNER JOIN Customer c ON b.customer.id  = c.id " +
            "WHERE b.creationTime < :currentDateTime AND c.id = :id ORDER BY b.creationTime ASC")
    List<Booking> findAllWithCreationTimeInThePast(@Param("currentDateTime") LocalDateTime currentDateTime, @Param("id") Integer id);

    @Query("SELECT b FROM Booking b WHERE  (:customerName IS NULL OR b.customer.name LIKE %:customerName%) AND  (:vehicleType IS NULL " +
            "OR b.vehicalType = :vehicleType) AND (:requestTime IS NULL OR b.requestTime = :requestTime)")
    List<Booking> findBookings(@Param("customerName") String customerName, @Param("vehicleType") String vehicleType, @Param("requestTime") LocalDateTime requestTime);
}
