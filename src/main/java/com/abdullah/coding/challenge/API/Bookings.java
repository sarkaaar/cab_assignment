package com.abdullah.coding.challenge.API;

import com.abdullah.coding.challenge.Services.RidesServices;
import com.abdullah.coding.challenge.entities.Booking;
import com.abdullah.coding.challenge.entities.Rating;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("api/ride")
public class Bookings {

    @Autowired
    private RidesServices rideService;
    @PostMapping("/bookRide")
    public String bookRide(){
        return "";
    }

    @GetMapping("/getFutureRides")
    public List<Booking> seeFutureRides(@RequestBody Booking ride) {
        List<Booking> futureBooking = rideService.seeFutureRides(ride.getCustomer().getId());
        return futureBooking;
    }


    @GetMapping("/getPastRides")
    public List<Booking> seePastRides(@RequestBody Booking ride) {
        List<Booking> pastBooking = rideService.seePastRides(ride.getCustomer().getId());
        return pastBooking;
    }

    @GetMapping("/getBookingInfo")
    public Optional<Booking> getBookingInfo(@RequestBody Booking ride) {
        Optional<Booking> bookingInfo = rideService.getBookingInfo(ride.getBookingCode());
        return bookingInfo;
    }

    @PostMapping("/addRating")
    public Rating addRating(@RequestBody Rating rating) {
        Rating addRating = rideService.addRating(rating);
        return addRating;
    }





}
