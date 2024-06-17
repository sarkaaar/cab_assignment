package com.abdullah.coding.challenge.API;

import com.abdullah.coding.challenge.Services.RidesServices;
import com.abdullah.coding.challenge.entities.Booking;
import com.abdullah.coding.challenge.entities.Cab;
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
    public Booking bookRide(@RequestBody Booking ride){
        return rideService.addBooking(ride);
    }

    @PostMapping("/updateRide")
    public Booking updateRide(@RequestBody Booking ride){
        return rideService.updateBooking(ride);
    }

    @PostMapping("/cancelRide")
    public Booking cancelRide(@RequestBody Booking ride){
        return rideService.cancelBooking(ride);
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
    public Cab addRating(@RequestBody Rating rating) {
        Cab addRating = rideService.addRating(rating);
        return addRating;
    }





}
