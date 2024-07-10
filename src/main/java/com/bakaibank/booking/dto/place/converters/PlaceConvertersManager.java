package com.bakaibank.booking.dto.place.converters;

import com.bakaibank.booking.dto.employee.EmployeeDTO;
import com.bakaibank.booking.dto.place.PlaceDTO;
import com.bakaibank.booking.dto.place.PlaceWithBookingDTO;
import com.bakaibank.booking.dto.position.PositionDTO;
import com.bakaibank.booking.dto.team.TeamDTO;
import com.bakaibank.booking.entity.Booking;
import com.bakaibank.booking.entity.Employee;
import com.bakaibank.booking.entity.Place;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;

import java.util.Optional;


public class PlaceConvertersManager {
    public static Converter<Place, PlaceDTO> placeToPlaceDTOConverter() {
        return new Converter<>() {
            @Override
            public PlaceDTO convert(MappingContext<Place, PlaceDTO> mappingContext) {
                Place place = mappingContext.getSource();

                return new PlaceDTO(
                        place.getId(),
                        place.getCode()
                );
            }
        };
    }

    public static Converter<Place, PlaceWithBookingDTO> placeToPlaceWithBookingDTOConverter() {
        return new Converter<>() {
            @Override
            public PlaceWithBookingDTO convert(MappingContext<Place, PlaceWithBookingDTO> mappingContext) {
                Place place = mappingContext.getSource();

                return new PlaceWithBookingDTO(
                        place.getId(),
                        place.getCode()
                );
            }
        };
    }

    public static Converter<Booking, PlaceWithBookingDTO.NestedBookingDTO> bookingToNestedBookingDTOConverter() {
        return new Converter<>() {
            @Override
            public PlaceWithBookingDTO.NestedBookingDTO convert(MappingContext<Booking, PlaceWithBookingDTO.NestedBookingDTO> mappingContext) {
                Booking booking = mappingContext.getSource();
                Employee employee = booking.getEmployee();

                return new PlaceWithBookingDTO.NestedBookingDTO(
                        booking.getBookingDate(),
                        new EmployeeDTO(
                                employee.getId(),
                                employee.getUsername(),
                                employee.getFirstName(),
                                employee.getLastName(),
                                employee.getMiddleName(),
                                Optional.ofNullable(employee.getPosition())
                                        .map(position -> new PositionDTO(position.getId(), position.getName()))
                                        .orElse(null),
                                Optional.ofNullable(employee.getTeam())
                                        .map(team -> new TeamDTO(team.getId(), team.getName()))
                                        .orElse(null)
                        )
                );
            }
        };
    }
}
