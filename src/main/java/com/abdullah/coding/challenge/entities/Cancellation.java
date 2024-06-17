package com.abdullah.coding.challenge.entities;

import lombok.Data;
import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Data
@Table(name = "cancellationLogs")
public class Cancellation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    Integer id;
    @Column(name = "booking_code")
    String bookingCode;
    @ManyToOne
    @JoinColumn(name = "booking_id")
    private Booking booking;
    @Column(name = "cancelled_by")
    private String cancelledBy;
    @Column(name = "reason")
    private String reason;
    @Column(name = "cancellation_time")
    private Timestamp cancellationTime;
}