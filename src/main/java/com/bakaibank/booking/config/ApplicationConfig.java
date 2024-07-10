package com.bakaibank.booking.config;

import com.bakaibank.booking.dto.employee.converters.EmployeeConvertersManager;
import com.bakaibank.booking.dto.place.converters.PlaceConvertersManager;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfig {
    private final EmployeeConvertersManager employeeConvertersManager;

    @Autowired
    public ApplicationConfig(EmployeeConvertersManager employeeConvertersManager) {
        this.employeeConvertersManager = employeeConvertersManager;
    }

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        modelMapper.addConverter(employeeConvertersManager.employeeToEmployeeDTOConverter());
        modelMapper.addConverter(employeeConvertersManager.createEmployeeDTOToEmployeeConverter());

        modelMapper.addConverter(PlaceConvertersManager.placeToPlaceDTOConverter());
        modelMapper.addConverter(PlaceConvertersManager.placeToPlaceWithBookingDTOConverter());
        modelMapper.addConverter(PlaceConvertersManager.bookingToNestedBookingDTOConverter());

        return modelMapper;
    }
}
