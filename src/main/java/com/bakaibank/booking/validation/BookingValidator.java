package com.bakaibank.booking.validation;

import com.bakaibank.booking.dto.booking.CreateBookingDTO;
import com.bakaibank.booking.entity.Weekend;
import com.bakaibank.booking.repository.BookingRepository;
import com.bakaibank.booking.repository.WeekendRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;

@Component
public class BookingValidator implements Validator {
    private final BookingRepository bookingRepository;

    private final WeekendRepository weekendRepository;

    @Value("${booking.open.time}")
    private String bookingOpenTimePattern;

    @Value("${booking.close.time}")
    private String bookingCloseTimePattern;

    @Value("${booking.allowed.days-ahead}")
    private int bookingAllowedDaysAhead;

    @Autowired
    public BookingValidator(BookingRepository bookingRepository, WeekendRepository weekendRepository) {
        this.bookingRepository = bookingRepository;
        this.weekendRepository = weekendRepository;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return CreateBookingDTO.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        CreateBookingDTO createBookingDTO = (CreateBookingDTO) target;
        LocalDate bookingDate = createBookingDTO.getBookingDate();

        if(!validateBookingTime(errors)) return;

        if(!validateBookingDate(bookingDate, errors)) return;


        if(bookingRepository.existsByPlace_IdAndBookingDate(createBookingDTO.getPlaceId(), bookingDate)) {
            errors.reject("placeIsAlreadyTaken", "Выбранное место уже забронировано на эту дату");
            return;
        }

        if(bookingRepository.existsByEmployee_IdAndBookingDate(createBookingDTO.getEmployeeId(), bookingDate)) {
            errors.reject("alreadyBookedAnotherPlace", "Вы уже забронировали место на выбранную дату");
        }
    }

    private boolean validateBookingTime(Errors errors) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        LocalTime bookingOpenTime = LocalTime.parse(this.bookingOpenTimePattern, formatter);
        LocalTime bookingCloseTime = LocalTime.parse(this.bookingCloseTimePattern, formatter);

        if(LocalTime.now().isBefore(bookingOpenTime) || LocalTime.now().isAfter(bookingCloseTime)) {
            errors.reject("invalidBookingTime", "Бронирование доступно только в период с "
                    + bookingOpenTimePattern + " до " + bookingCloseTimePattern);
            return false;
        }

        return true;
    }

    private boolean validateBookingDate(LocalDate bookingDate, Errors errors) {
        LocalDate now = LocalDate.now();

        if(bookingDate.isBefore(now) || bookingDate.equals(now)) {
            errors.reject("invalidBookingDate", "Нельзя забронировать место на прошедшую или текущую дату");
            return false;
        }

        if(isWeekend(bookingDate)) {
            errors.reject("dateIsWeekend", "Нельзя забронировать место на выходной день");
            return false;
        }

        if(ChronoUnit.DAYS.between(now, bookingDate) > bookingAllowedDaysAhead) {
            LocalDate nextWorkingDate = getNextWorkingDate(now);

            if(!bookingDate.equals(nextWorkingDate)) {
                errors.reject("tooMuchDaysAhead", "Вы можете сделать бронь максимум на " + nextWorkingDate);
                return false;
            }

        }

        return true;
    }

    private boolean isWeekend(LocalDate date) {
        return weekendRepository.existsByDate(date);
    }

    private LocalDate getNextWorkingDate(LocalDate currentDate) {
        List<LocalDate> weekends = weekendRepository.findAllByDateIsGreaterThanEqualOrderByDateAsc(currentDate).stream()
                .map(Weekend::getDate)
                .toList();

        do
            currentDate = currentDate.plusDays(1);
        while (Collections.binarySearch(weekends, currentDate) >= 0);

        return currentDate;
    }
}
