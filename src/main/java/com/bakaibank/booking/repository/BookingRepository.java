package com.bakaibank.booking.repository;

import com.bakaibank.booking.entity.Booking;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    @EntityGraph(value = "Booking.withFullEmployeeInfo", type = EntityGraph.EntityGraphType.LOAD)
    Optional<Booking> findByPlace_IdAndBookingDate(Long placeId, LocalDate date);

    @EntityGraph(value = "Booking.withFullEmployeeInfo", type = EntityGraph.EntityGraphType.LOAD)
    List<Booking> findAllByBookingDate(LocalDate date);

    @EntityGraph(value = "Booking.withFullEmployeeInfo", type = EntityGraph.EntityGraphType.LOAD)
    @Query("SELECT b FROM Booking b WHERE b.id = :id")
    Optional<Booking> findByIdWithFullEmployeeInfo(@Param("id") Long id);

    boolean existsByPlace_IdAndBookingDate(Long placeId, LocalDate bookingDate);

    boolean existsByEmployee_IdAndBookingDate(Long employeeId, LocalDate bookingDate);
}
