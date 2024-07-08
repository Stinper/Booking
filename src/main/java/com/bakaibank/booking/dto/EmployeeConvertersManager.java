package com.bakaibank.booking.dto;

import com.bakaibank.booking.entity.Employee;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;

public class EmployeeConvertersManager {
    public static Converter<Employee, EmployeeDTO> employeeToEmployeeDTOConverter() {
        return new Converter<>() {
            @Override
            public EmployeeDTO convert(MappingContext<Employee, EmployeeDTO> mappingContext) {
                Employee employee = mappingContext.getSource();

                return new EmployeeDTO(
                        employee.getId(),
                        employee.getUsername(),
                        employee.getFirstName(),
                        employee.getLastName(),
                        employee.getMiddleName()
                );
            }
        };
    }

    public static Converter<Employee, CreateEmployeeDTO> employeeToCreateEmployeeDTOConverter() {
        return new Converter<>() {
            @Override
            public CreateEmployeeDTO convert(MappingContext<Employee, CreateEmployeeDTO> mappingContext) {
                Employee employee = mappingContext.getSource();

                return new CreateEmployeeDTO(
                        employee.getUsername(),
                        employee.getEmail(),
                        employee.getPassword(),
                        employee.getFirstName(),
                        employee.getLastName(),
                        employee.getMiddleName()
                );
            }
        };
    }
}
