package com.bakaibank.booking.config;

import com.bakaibank.booking.dto.EmployeeConvertersManager;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfig {
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        modelMapper.addConverter(EmployeeConvertersManager.employeeToEmployeeDTOConverter());
        modelMapper.addConverter(EmployeeConvertersManager.employeeToCreateEmployeeDTOConverter());

        return modelMapper;
    }
}
