package com.bakaibank.booking.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Table(name = "bookings")
@Data
@NamedEntityGraphs({
        @NamedEntityGraph(
                name = "Booking.withFullEmployeeInfo",
                attributeNodes = {
                        @NamedAttributeNode(value = "employee", subgraph = "employee-position-team")
                },
                subgraphs = {
                        @NamedSubgraph(
                                name = "employee-position-team",
                                attributeNodes = {
                                        @NamedAttributeNode("position"),
                                        @NamedAttributeNode("team")
                                }
                        )
                }
        )
})
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "place_id", nullable = false)
    private Place place;

    @Column(name = "booking_date", nullable = false)
    private LocalDate bookingDate;

}
