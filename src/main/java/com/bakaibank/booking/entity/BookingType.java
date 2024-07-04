package com.bakaibank.booking.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "booking_types")
@Data
public class BookingType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "type", unique = true, nullable = false)
    private String type;

    @OneToMany(mappedBy = "type", fetch = FetchType.LAZY)
    private List<Booking> bookings = new ArrayList<>();
}
