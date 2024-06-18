package com.abdullah.coding.challenge.API;

import com.abdullah.coding.challenge.Services.RidesServices;
import com.abdullah.coding.challenge.entities.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("api/ride")
public class Bookings {

    @Autowired
    private RidesServices rideService;

    @PostMapping("/bookRide")
    public ResponseEntity<Booking> bookRide(@RequestBody Booking ride) {
        Booking rideInfo = rideService.addBooking(ride);

        if (rideService.addBooking(ride).equals(null))
            return new ResponseEntity<>(rideInfo, HttpStatus.BAD_REQUEST);
        else
            return new ResponseEntity<>(rideInfo, HttpStatus.OK);
//        return rideService.addBooking(ride);
    }

    @PostMapping("/updateRide")
    public ResponseEntity<Booking> updateRide(@RequestBody Booking ride) {
        Booking rideInfo = rideService.updateBooking(ride);
        if (rideService.addBooking(ride).equals(null))
            return new ResponseEntity<>(rideInfo, HttpStatus.BAD_REQUEST);
        else
            return new ResponseEntity<>(rideInfo, HttpStatus.OK);
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
    public ResponseEntity<Booking> getBookingInfo(@RequestBody Booking ride) {
        Booking bookingInfo = rideService.getBookingInfo(ride.getBookingCode());
        if (bookingInfo == null )
            return new ResponseEntity<>(bookingInfo, HttpStatus.NOT_FOUND);
        else
            return new ResponseEntity<>(bookingInfo, HttpStatus.OK);
    }

    @PostMapping("/addRating")
    public Cab addRating(@RequestBody Rating rating) {
        return rideService.addRating(rating);
    }

    @GetMapping
    public ResponseEntity<List<Booking>> getBookings(@RequestBody SearchBean criteria) {
        List<Booking> bookings = rideService.findBookings(criteria.getCustName(), criteria.getVehileType(), criteria.getRequestTime());
        if (bookings.isEmpty())
            return new ResponseEntity<>(bookings, HttpStatus.NOT_FOUND);
        else
            return new ResponseEntity<>(bookings, HttpStatus.OK);    }

    @PutMapping("/{bookingId}/assignCab/{cabId}")
    public ResponseEntity<Booking> assignCabToBooking(@PathVariable Integer bookingId, @PathVariable Integer cabId) {
        Booking updatedBooking = rideService.assignCabToBooking(bookingId, cabId);
        return ResponseEntity.ok(updatedBooking);
    }

}
