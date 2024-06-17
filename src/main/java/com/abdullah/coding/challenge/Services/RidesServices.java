package com.abdullah.coding.challenge.Services;

import com.abdullah.coding.challenge.Repositories.*;
import com.abdullah.coding.challenge.entities.*;
import com.abdullah.coding.challenge.enums.BookingStatus;
import com.abdullah.coding.challenge.enums.CustomerStatus;
import com.abdullah.coding.challenge.enums.UserStatus;
import com.abdullah.coding.challenge.enums.VehicleType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.UUID;

@Service
public class RidesServices {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private RatingRepository ratingRepository;

    @Autowired
    private CabRepository cabRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CancellationRepository cancelRepository;

    public Booking addBooking(Booking booking) {
        UUID uuid = UUID.randomUUID();
        Optional<Customer> optionalCustomer = customerRepository.findById(booking.getCustomer().getId());

        if (optionalCustomer.isPresent() && optionalCustomer.get().getStatus().equals(CustomerStatus.ACTIVE)) {
            Booking newBooking = new Booking();
            newBooking.setCustomer(booking.getCustomer());
            newBooking.setStatus(BookingStatus.REQUESTED.name());
            newBooking.setCreationTime(Timestamp.valueOf(LocalDateTime.now()));
            newBooking.setRequestTime(booking.getRequestTime());

            try {
                VehicleType vehicleType = VehicleType.valueOf(booking.getVehicalType());
                newBooking.setVehicalType(vehicleType.name());
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
                return null;
            }

            newBooking.setBookingCode(uuid.toString());
            return bookingRepository.save(newBooking);
        } else {
            return null;
        }
    }

    public Booking updateBooking(Booking booking) {
        Optional<Booking> optionalRide = Optional.ofNullable(bookingRepository.findByBookingCode(booking.getBookingCode()));

        if (!optionalRide.isPresent()) {
            return null;
        }

        Booking ride = optionalRide.get();
        boolean updated = false;

        if (booking.getRequestTime() != null && !booking.getRequestTime().equals(ride.getRequestTime())) {
            ride.setRequestTime(booking.getRequestTime());
            updated = true;
        }

        try {
            VehicleType newVehicleType = VehicleType.valueOf(booking.getVehicalType());
            if (!newVehicleType.equals(VehicleType.valueOf(ride.getVehicalType()))) {
                ride.setVehicalType(booking.getVehicalType());
                updated = true;
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return null;
        }

        if (updated) {
            return bookingRepository.save(ride);
        } else {
            return ride;
        }
    }

    public List<Booking> findBookings(String customerName, String vehicleType, LocalDateTime requestTime) {
        return bookingRepository.findBookings(customerName, vehicleType, requestTime);
    }

    public Booking cancelBooking(Cancellation canceled) {
        Optional<Booking> optionalBooking = Optional.ofNullable(bookingRepository.findByBookingCode(canceled.getBookingCode()));

        if (!optionalBooking.isPresent()) {
            return null;
        }

        Booking booking = optionalBooking.get();

        if (booking.getStatus().equals(BookingStatus.CANCELLED.name()) || booking.getStatus().equals(BookingStatus.COMPLETED.name())) {
            throw new IllegalStateException("Booking is already cancelled or completed.");
        }

        booking.setStatus(BookingStatus.CANCELLED.name());
        Booking updatedBooking = bookingRepository.save(booking);

        Optional<Customer> optionalCustomer = customerRepository.findById(booking.getCustomer().getId());
        Cancellation cancellationLog = new Cancellation();
        if (optionalCustomer.isPresent()) {
            cancellationLog.setCancelledBy(UserStatus.USER.name());
        } else {
            cancellationLog.setCancelledBy(UserStatus.ADMIN.name());
        }
        cancellationLog.setBooking(booking);
        cancellationLog.setReason(canceled.getReason());
        cancellationLog.setCancellationTime(Timestamp.valueOf(LocalDateTime.now()));
        cancelRepository.save(cancellationLog);

        return updatedBooking;
    }

    public List<Booking> seeFutureRides(Integer customerId) {
        LocalDateTime timestamp = LocalDateTime.now();
        List<Booking> futureRides = bookingRepository.findAllWithCreationTimeInTheFuture(timestamp, customerId);
        return futureRides;
    }


    public List<Booking> seePastRides(Integer customerId) {
        LocalDateTime timestamp = LocalDateTime.now();
        List<Booking> pastRides = bookingRepository.findAllWithCreationTimeInThePast(timestamp, customerId);
        return pastRides;
    }

    public Optional<Booking> getBookingInfo(String bookingCode) {
        Optional<Booking> rideInfo = Optional.ofNullable(bookingRepository.findByBookingCode(bookingCode));
        return rideInfo;
    }

    public Cab addRating(Rating rating) {
        Cab savedRec = null;

        try {
            ratingRepository.save(rating);
            double averageRating = calculateAverageRating(rating.getCabId());
            Cab cab = cabRepository.findById(rating.getId()).orElseThrow(() -> new RuntimeException("Cab not found"));
            cab.setRating(averageRating);
            savedRec = cabRepository.save(cab);
        } catch (Exception exp) {
            exp.printStackTrace();
            return new Cab();
        }
        return savedRec;
    }

    public double calculateAverageRating(Integer cabId) {
        List<Integer> ratings = ratingRepository.findRatingsByCabId(cabId);
        OptionalDouble average = ratings.stream().mapToInt(r -> r).average();
        return average.isPresent() ? average.getAsDouble() : 0.0;
    }
}
