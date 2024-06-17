package com.abdullah.coding.challenge.Services;

import com.abdullah.coding.challenge.Repositories.BookingRepository;
import com.abdullah.coding.challenge.Repositories.RatingRepository;
import com.abdullah.coding.challenge.entities.Booking;
import com.abdullah.coding.challenge.entities.Cab;
import com.abdullah.coding.challenge.entities.Rating;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class RidesServices {

    @Autowired
    private BookingRepository bookingRepository;

    private RatingRepository ratingRepository;
    public void BookedBooking(){

    }

    public List<Booking> seeFutureRides(Integer customerId){
        LocalDateTime timestamp = LocalDateTime.now();
        List<Booking> futureRides =  bookingRepository.findAllWithCreationTimeInTheFuture(timestamp, customerId);
        return futureRides;
    }


    public List<Booking> seePastRides(Integer customerId){
        LocalDateTime timestamp = LocalDateTime.now();
        List<Booking> pastRides =  bookingRepository.findAllWithCreationTimeInThePast(timestamp, customerId);
        return pastRides;
    }

    public Optional<Booking> getBookingInfo(String bookingCode){
        Optional<Booking> rideInfo = Optional.ofNullable(bookingRepository.findByBookingCode(bookingCode));
        return rideInfo;
    }


    public Rating addRating(Rating rating){

        Rating rat = ratingRepository.save(rating);










//        Optional<Booking> pastRides = Optional.ofNullable(bookingRepository.findByBookingCode(bookingCode));
        return new Rating();
    }
}
