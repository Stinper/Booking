package com.bakaibank.booking.repository;

import com.bakaibank.booking.entity.Place;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface PlaceRepository extends JpaRepository<Place, Long> {

    @Query("SELECT p, CASE WHEN EXISTS (SELECT b FROM Booking b WHERE b.place = p AND b.bookingDate = :date) THEN true ELSE false END " +
            "FROM Place p")
    List<Object[]> findAllWithBookingByDate(@Param("date") LocalDate date);
}
