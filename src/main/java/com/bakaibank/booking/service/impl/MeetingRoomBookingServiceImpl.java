package com.bakaibank.booking.service.impl;

import com.bakaibank.booking.core.BookingUserDetails;
import com.bakaibank.booking.dto.booking.rooms.CreateMeetingRoomBookingDTO;
import com.bakaibank.booking.dto.booking.rooms.MeetingRoomBookingDTO;
import com.bakaibank.booking.entity.MeetingRoomBooking;
import com.bakaibank.booking.exceptions.ValidationException;
import com.bakaibank.booking.repository.MeetingRoomBookingRepository;
import com.bakaibank.booking.service.MeetingRoomBookingService;
import com.bakaibank.booking.validation.MeetingRoomBookingValidator;
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
public class MeetingRoomBookingServiceImpl implements MeetingRoomBookingService {
    private final MeetingRoomBookingRepository meetingRoomBookingRepository;
    private final ModelMapper modelMapper;
    private final MeetingRoomBookingValidator meetingRoomBookingValidator;

    @Autowired
    public MeetingRoomBookingServiceImpl(MeetingRoomBookingRepository meetingRoomBookingRepository,
                                         ModelMapper modelMapper,
                                         MeetingRoomBookingValidator meetingRoomBookingValidator) {
        this.meetingRoomBookingRepository = meetingRoomBookingRepository;
        this.modelMapper = modelMapper;
        this.meetingRoomBookingValidator = meetingRoomBookingValidator;
    }

    @Override
    public List<MeetingRoomBookingDTO> findAllByDate(LocalDate date) {
        List<MeetingRoomBooking> meetingRoomBookings = meetingRoomBookingRepository.findAllByDate(date);

        return meetingRoomBookings.stream()
                .map(meetingRoomBooking -> modelMapper.map(meetingRoomBooking, MeetingRoomBookingDTO.class))
                .toList();
    }

    @Override
    public List<MeetingRoomBookingDTO> findAllByMeetingRoomIdAndDate(Long meetingRoomId, LocalDate date) {
        List<MeetingRoomBooking> meetingRoomBookings = meetingRoomBookingRepository
                .findAllByMeetingRoom_IdAndDate(meetingRoomId, date);

        return meetingRoomBookings.stream()
                .map(meetingRoomBooking -> modelMapper.map(meetingRoomBooking, MeetingRoomBookingDTO.class))
                .toList();
    }

    @Override
    public Optional<MeetingRoomBookingDTO> findById(Long id) {
        return meetingRoomBookingRepository.findById(id)
                .map(meetingRoomBooking -> modelMapper.map(meetingRoomBooking, MeetingRoomBookingDTO.class));
    }

    @Override
    public MeetingRoomBookingDTO save(@Valid CreateMeetingRoomBookingDTO createMeetingRoomBookingDTO,
                                      BookingUserDetails userDetails) {
        createMeetingRoomBookingDTO.setEmployeeId(userDetails.getId());
        //Добавляем организатора встречи в список участников, чтобы не валидировать его отдельно.
        //Если он уже был добавлен, ничего не произойдет, т.к. это Set
        createMeetingRoomBookingDTO.getParticipants().add(userDetails.getUsername());

        Errors errors = new BeanPropertyBindingResult(createMeetingRoomBookingDTO, "meetingRoomBookingErrors");
        meetingRoomBookingValidator.validate(createMeetingRoomBookingDTO, errors);

        if(errors.hasErrors())
            throw new ValidationException(errors);

        MeetingRoomBooking meetingRoomBooking = meetingRoomBookingRepository.save(
                modelMapper.map(createMeetingRoomBookingDTO, MeetingRoomBooking.class));

        return modelMapper.map(meetingRoomBooking, MeetingRoomBookingDTO.class);
    }

    @Override
    public void deleteById(Long id) {
        meetingRoomBookingRepository.deleteById(id);
    }
}
