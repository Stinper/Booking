package com.bakaibank.booking.validation;

import com.bakaibank.booking.dto.booking.CreateBookingDTO;
import com.bakaibank.booking.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Component
public class BookingValidator implements Validator {
    private final BookingRepository bookingRepository;

    @Value("${booking.open.time}")
    private String bookingOpenTimePattern;

    @Value("${booking.close.time}")
    private String bookingCloseTimePattern;

    @Value("${booking.allowed.days-ahead}")
    private int bookingAllowedDaysAhead;

    @Autowired
    public BookingValidator(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
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
        if(bookingDate.isBefore(LocalDate.now()) || bookingDate.equals(LocalDate.now())) {
            errors.reject("invalidBookingDate", "Нельзя забронировать место на прошедшую или текущую дату");
            return false;
        }

        if(isWeekend(bookingDate)) {
            errors.reject("dateIsWeekend", "Нельзя забронировать место на выходной день");
            return false;
        }

        if(bookingDate.isAfter(LocalDate.now().plusDays(bookingAllowedDaysAhead))) {
            //TODO: После того, как привяжем рабочий календарь, нужно позволить бронировать на
            // следующий рабочий день, если между текущим и следующим есть выходные дни и между ними больше дней, чем {bookingAllowedDaysAhead}
            errors.reject("tooMuchDaysAhead", "Бронирование доступно максимум на " + bookingAllowedDaysAhead + " день вперед");
            return false;
        }

        return true;
    }

    private boolean isWeekend(LocalDate bookingDate) {
        //TODO: Потом привязать сюда календарь рабочих дней

        DayOfWeek dayOfWeek = bookingDate.getDayOfWeek();
        return dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY;
    }
}
