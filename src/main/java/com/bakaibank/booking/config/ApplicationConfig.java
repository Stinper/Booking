package com.bakaibank.booking.config;

import com.bakaibank.booking.dto.booking.places.converters.BookingConvertersManager;
import com.bakaibank.booking.dto.booking.rooms.converters.MeetingRoomBookingConvertersManager;
import com.bakaibank.booking.dto.employee.converters.EmployeeConvertersManager;
import com.bakaibank.booking.dto.meetingroom.converters.MeetingRoomConvertersManager;
import com.bakaibank.booking.dto.place.converters.PlaceConvertersManager;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {
    private final EmployeeConvertersManager employeeConvertersManager;
    private final BookingConvertersManager bookingConvertersManager;
    private final MeetingRoomBookingConvertersManager meetingRoomBookingConvertersManager;

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        modelMapper.addConverter(EmployeeConvertersManager.employeeToEmployeeDTOConverter());
        modelMapper.addConverter(employeeConvertersManager.createEmployeeDTOToEmployeeConverter());

        modelMapper.addConverter(PlaceConvertersManager.placeToPlaceDTOConverter());
        modelMapper.addConverter(PlaceConvertersManager.placeToPlaceWithBookingDTOConverter());
        modelMapper.addConverter(PlaceConvertersManager.bookingToNestedBookingDTOConverter());

        modelMapper.addConverter(BookingConvertersManager.bookingToBookingDTOConverter());
        modelMapper.addConverter(bookingConvertersManager.createBookingDTOToBookingConverter());

        modelMapper.addConverter(MeetingRoomConvertersManager.meetingRoomToMeetingRoomDTOConverter());
        modelMapper.addConverter(MeetingRoomConvertersManager.createMeetingRoomDTOToMeetingRoomConverter());

        modelMapper.addConverter(MeetingRoomBookingConvertersManager.meetingRoomBookingToMeetingRoomBookingDTOConverter());
        modelMapper.addConverter(meetingRoomBookingConvertersManager.createMeetingRoomBookingDTOToMeetingRoomBookingConverter());

        return modelMapper;
    }
}
