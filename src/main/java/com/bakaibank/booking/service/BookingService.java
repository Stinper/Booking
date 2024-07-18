package com.bakaibank.booking.service;

import com.bakaibank.booking.core.BookingUserDetails;
import com.bakaibank.booking.dto.booking.places.BookingDTO;
import com.bakaibank.booking.dto.booking.places.CreateBookingDTO;
import jakarta.validation.Valid;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


public interface BookingService {
    List<BookingDTO> findAllByDate(LocalDate date);
    Optional<BookingDTO> findById(Long id);
    BookingDTO save(@Valid CreateBookingDTO createBookingDTO, BookingUserDetails userDetails);
    void deleteById(Long id);
}
