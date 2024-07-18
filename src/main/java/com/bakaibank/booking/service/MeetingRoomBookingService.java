package com.bakaibank.booking.service;

import com.bakaibank.booking.core.BookingUserDetails;
import com.bakaibank.booking.dto.booking.rooms.CreateMeetingRoomBookingDTO;
import com.bakaibank.booking.dto.booking.rooms.MeetingRoomBookingDTO;
import jakarta.validation.Valid;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface MeetingRoomBookingService {
    List<MeetingRoomBookingDTO> findAllByDate(LocalDate date);

    List<MeetingRoomBookingDTO> findAllByMeetingRoomIdAndDate(Long meetingRoomId, LocalDate date);
    Optional<MeetingRoomBookingDTO> findById(Long id);
    MeetingRoomBookingDTO save(@Valid CreateMeetingRoomBookingDTO createMeetingRoomBookingDTO,
                               BookingUserDetails userDetails);
    void deleteById(Long id);
}
