package com.bakaibank.booking.validation;

import com.bakaibank.booking.dto.booking.places.CreateBookingDTO;
import com.bakaibank.booking.repository.BookingRepository;
import com.bakaibank.booking.repository.WeekendRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.time.LocalDate;

@Component
public class BookingValidator extends AbstractBookingValidator implements Validator {
    private final BookingRepository bookingRepository;

    @Value("${booking.places.allowed.days-ahead}")
    private int placeBookingAllowedDaysAhead;

    @Autowired
    public BookingValidator(BookingRepository bookingRepository, WeekendRepository weekendRepository) {
        super(weekendRepository);
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

        if(!validateBookingOpenCloseTime(errors)) return;

        if(!validateBookingDate(bookingDate, placeBookingAllowedDaysAhead, errors)) return;


        if(bookingRepository.existsByPlace_IdAndBookingDate(createBookingDTO.getPlaceId(), bookingDate)) {
            errors.reject("placeIsAlreadyTaken", "Выбранное место уже забронировано на эту дату");
            return;
        }

        if(bookingRepository.existsByEmployee_IdAndBookingDate(createBookingDTO.getEmployeeId(), bookingDate)) {
            errors.reject("alreadyBookedAnotherPlace", "Вы уже забронировали место на выбранную дату");
        }
    }
}
