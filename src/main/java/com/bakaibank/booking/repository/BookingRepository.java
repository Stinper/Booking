package com.bakaibank.booking.repository;

import com.bakaibank.booking.entity.Booking;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    @EntityGraph(value = "Booking.withFullEmployeeInfo", type = EntityGraph.EntityGraphType.LOAD)
    Optional<Booking> findByPlace_IdAndBookingDate(Long placeId, LocalDate date);
}
