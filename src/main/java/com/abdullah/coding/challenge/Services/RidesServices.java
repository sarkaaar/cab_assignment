package com.abdullah.coding.challenge.Services;

import com.abdullah.coding.challenge.Repositories.*;
import com.abdullah.coding.challenge.Utils.Utils;
import com.abdullah.coding.challenge.entities.*;
import com.abdullah.coding.challenge.enums.*;
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

        if (optionalCustomer.isPresent() && optionalCustomer.get().getStatus().equals(CustomerStatus.ACTIVE.name())) {
            Customer managedCustomer = optionalCustomer.get();
            Booking newBooking = new Booking();

            try {
                newBooking.setCustomer(managedCustomer);
                newBooking.setStatus(BookingStatus.REQUESTED.name());
                newBooking.setCreationTime(Timestamp.valueOf(LocalDateTime.now()));
                if (Utils.validateFutureTimeStamp(booking.getRequestTime())) {
                    newBooking.setRequestTime(booking.getRequestTime());
                } else {
                    return null;
                }

                newBooking.setBookingCode(uuid.toString());
                VehicleType vehicleType = VehicleType.valueOf(booking.getVehicalType());
                newBooking.setVehicalType(vehicleType.name());
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
                return null;
            }
            return bookingRepository.save(newBooking);
        } else {
            return null;
        }
    }

    public Booking updateBooking(Booking booking) {
        Optional<Booking> optionalRide = Optional.ofNullable(bookingRepository.findByBookingCode(booking.getBookingCode()));

        if (optionalRide.isEmpty()) {
            return null;
        }

        Booking ride = optionalRide.get();
        boolean updated = false;

        if (BookingStatus.valueOf(ride.getStatus()).name().equals(BookingStatus.REQUESTED.name()) || BookingStatus.valueOf(ride.getStatus()).name().equals(BookingStatus.ASSIGNED.name())) {

            try {
                String newVehicleType = VehicleType.valueOf(booking.getVehicalType()).name();

                if (Utils.validateFutureTimeStamp(booking.getRequestTime()) && !booking.getRequestTime().equals(ride.getRequestTime())) {
                    ride.setRequestTime(booking.getRequestTime());
                    ride.setStatus(BookingStatus.REQUESTED.name());
                    updated = true;
                }

                if (!newVehicleType.equals(VehicleType.valueOf(ride.getVehicalType()).name())) {
                    ride.setVehicalType(booking.getVehicalType());
                    ride.setStatus(BookingStatus.REQUESTED.name());
                    updated = true;
                }
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
                return null;
            }
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

        if (optionalBooking.isEmpty()) {
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
        return bookingRepository.findAllUpcommingRides(timestamp, customerId);
    }


    public List<Booking> seePastRides(Integer customerId) {
        LocalDateTime timestamp = LocalDateTime.now();
        return bookingRepository.findAllPastRides(timestamp, customerId);
    }

    public Booking getBookingInfo(String bookingCode) {
        Optional<Booking> rideInfo = Optional.ofNullable(bookingRepository.findByBookingCode(bookingCode));
        return rideInfo.orElse(null);
    }

    public Cab addRating(Rating rating) {
        Cab savedRec;

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

    public Booking assignCabToBooking(Integer bookingId, Integer cabId) {
        Optional<Booking> booking = bookingRepository.findById(bookingId);
        Optional<Cab> cab = cabRepository.findById(cabId);

        try {
            if ((booking.isEmpty() || !booking.get().getStatus().equals(BookingStatus.ASSIGNED.name()))
                    && CustomerStatus.valueOf(booking.get().getCustomer().getStatus()).name().equals(CustomerStatus.INACTIVE.name())) {
                return null;
            }

            if (cab.isEmpty() || !cab.get().getStatus().equals(CabStatus.AVAILABLE.name())) {
                return null;
            }
        } catch (Exception exp) {
            exp.printStackTrace();
            return null;
        }

        booking.get().setCab(cab.get());
        return bookingRepository.save(booking.get());
    }
}
