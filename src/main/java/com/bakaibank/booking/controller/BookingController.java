package com.bakaibank.booking.controller;

import com.bakaibank.booking.core.BookingUserDetails;
import com.bakaibank.booking.dto.booking.places.BookingDTO;
import com.bakaibank.booking.dto.booking.places.CreateBookingDTO;
import com.bakaibank.booking.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/bookings/places")
public class BookingController {
    private final BookingService bookingService;

    @Autowired
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @GetMapping
    public List<BookingDTO> findAllBookings(@RequestParam(value = "date", required = false)
                                                @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                                                LocalDate date) {
        return bookingService.findAllByDate(date != null ? date : LocalDate.now());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookingDTO> findBookingById(@PathVariable Long id) {
        return bookingService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public BookingDTO createBooking(@RequestBody CreateBookingDTO createBookingDTO,
                                    @AuthenticationPrincipal BookingUserDetails userDetails) {
        return bookingService.save(createBookingDTO, userDetails);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBookingById(@PathVariable Long id) {
        return bookingService.findById(id)
                .map(booking -> {
                    bookingService.deleteById(id);
                    return ResponseEntity.ok(booking);
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
