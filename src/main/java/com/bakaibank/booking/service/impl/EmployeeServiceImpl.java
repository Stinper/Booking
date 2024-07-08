package com.bakaibank.booking.service.impl;

import com.bakaibank.booking.dto.CreateEmployeeDTO;
import com.bakaibank.booking.dto.EmployeeDTO;
import com.bakaibank.booking.entity.Employee;
import com.bakaibank.booking.repository.EmployeeRepository;
import com.bakaibank.booking.service.EmployeeService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Optional;

@Service
@Validated
public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeRepository employeeRepository;

    private final ModelMapper modelMapper;

    @Autowired
    public EmployeeServiceImpl(EmployeeRepository employeeRepository,
                               ModelMapper modelMapper) {
        this.employeeRepository = employeeRepository;
        this.modelMapper = modelMapper;
    }
    @Override
    public List<EmployeeDTO> findAll() {
        List<Employee> employees = employeeRepository.findAll();
        return employees.stream()
                .map(employee -> modelMapper.map(employee, EmployeeDTO.class))
                .toList();
    }

    @Override
    public Optional<EmployeeDTO> findById(Long id) {
        return employeeRepository.findById(id)
                .map(employee -> modelMapper.map(employee, EmployeeDTO.class));
    }

    @Override
    public EmployeeDTO save(@Valid CreateEmployeeDTO createEmployeeDTO) {
        Employee employee = employeeRepository.save(modelMapper.map(createEmployeeDTO, Employee.class));
        return modelMapper.map(employee, EmployeeDTO.class);
    }

    @Override
    public void deleteById(Long id) {
        employeeRepository.deleteById(id);
    }
}
