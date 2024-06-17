package com.abdullah.coding.challenge.API;

import com.abdullah.coding.challenge.Services.RidesServices;
import com.abdullah.coding.challenge.entities.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public Booking bookRide(@RequestBody Booking ride) {
        return rideService.addBooking(ride);
    }

    @PostMapping("/updateRide")
    public Booking updateRide(@RequestBody Booking ride) {
        return rideService.updateBooking(ride);
    }

    @PostMapping("/cancelRide")
    public Booking cancelRide(@RequestBody Cancellation ride) {
        return rideService.cancelBooking(ride);
    }

    @GetMapping("/getFutureRides")
    public ResponseEntity<List<Booking>> seeFutureRides(@RequestBody Booking ride) {
        List<Booking> futureBooking = rideService.seeFutureRides(ride.getCustomer().getId());
        if (futureBooking.isEmpty())
            return new ResponseEntity<>(futureBooking, HttpStatus.NOT_FOUND);
        else
            return new ResponseEntity<>(futureBooking, HttpStatus.OK);
    }

    @GetMapping("/getPastRides")
    public ResponseEntity<List<Booking>> seePastRides(@RequestBody Booking ride) {
        List<Booking> pastBooking = rideService.seePastRides(ride.getCustomer().getId());

        if (pastBooking.isEmpty())
            return new ResponseEntity<>(pastBooking, HttpStatus.NOT_FOUND);
        else
            return new ResponseEntity<>(pastBooking, HttpStatus.OK);
    }

    @GetMapping("/getBookingInfo")
    public ResponseEntity<Optional<Booking>> getBookingInfo(@RequestBody Booking ride) {
        Optional<Booking> bookingInfo = rideService.getBookingInfo(ride.getBookingCode());
        if (bookingInfo.isEmpty())
            return new ResponseEntity<>(bookingInfo, HttpStatus.NOT_FOUND);
        else
            return new ResponseEntity<>(bookingInfo, HttpStatus.OK);
    }

    @PostMapping("/addRating")
    public Cab addRating(@RequestBody Rating rating) {
        Cab addRating = rideService.addRating(rating);
        return addRating;
    }

    @GetMapping
    public ResponseEntity<List<Booking>> getBookings(@RequestBody SearchBean criteria) {
        List<Booking> bookings = rideService.findBookings(criteria.getCustName(), criteria.getVehileType(), criteria.getRequestTime());
        if (bookings.isEmpty())
            return new ResponseEntity<>(bookings, HttpStatus.NOT_FOUND);
        else
            return new ResponseEntity<>(bookings, HttpStatus.OK);    }

}
