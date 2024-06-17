package com.abdullah.coding.challenge.entities;

import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Data
@Table(name = "rating")
public class Rating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    Integer id;
    @Column(name = "customer_id")
    Integer customerId;
    @Column(name = "cab_id")
    Integer cabId;
    @Column(name = "creation_time")
    Timestamp creationTime;
    @Column(name = "rating")
    Integer rating;
    @Column(name = "comment")
    String comment;
}
