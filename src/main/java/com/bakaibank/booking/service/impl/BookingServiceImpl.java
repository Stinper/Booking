package com.bakaibank.booking.service.impl;

import com.bakaibank.booking.core.BookingUserDetails;
import com.bakaibank.booking.dto.booking.BookingDTO;
import com.bakaibank.booking.dto.booking.CreateBookingDTO;
import com.bakaibank.booking.entity.Booking;
import com.bakaibank.booking.exceptions.ValidationException;
import com.bakaibank.booking.repository.BookingRepository;
import com.bakaibank.booking.service.BookingService;
import com.bakaibank.booking.validation.BookingValidator;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Validated
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;

    private final ModelMapper modelMapper;

    private final BookingValidator bookingValidator;

    @Autowired
    public BookingServiceImpl(BookingRepository bookingRepository,
                              ModelMapper modelMapper,
                              BookingValidator bookingValidator) {
        this.bookingRepository = bookingRepository;
        this.modelMapper = modelMapper;
        this.bookingValidator = bookingValidator;
    }
    @Override
    public List<BookingDTO> findAllByDate(LocalDate date) {
        List<Booking> bookings = bookingRepository.findAllByBookingDate(date);
        return bookings.stream()
                .map(booking -> modelMapper.map(booking, BookingDTO.class))
                .toList();
    }

    @Override
    public Optional<BookingDTO> findById(Long id) {
        return bookingRepository.findByIdWithFullEmployeeInfo(id)
                .map(booking -> modelMapper.map(booking, BookingDTO.class));
    }

    @Override
    public BookingDTO save(@Valid CreateBookingDTO createBookingDTO, BookingUserDetails userDetails) {
        createBookingDTO.setEmployeeId(userDetails.getId());

        Errors errors = new BeanPropertyBindingResult(createBookingDTO, "bookingErrors");
        bookingValidator.validate(createBookingDTO, errors);

        if (errors.hasErrors())
            throw new ValidationException(errors);

        Booking booking = bookingRepository.save(modelMapper.map(createBookingDTO, Booking.class));
        return modelMapper.map(booking, BookingDTO.class);
    }

    @Override
    public void deleteById(Long id) {
        bookingRepository.deleteById(id);
    }
}
