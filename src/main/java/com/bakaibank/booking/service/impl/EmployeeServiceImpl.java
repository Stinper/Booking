package com.bakaibank.booking.service.impl;

import com.bakaibank.booking.dto.employee.CreateEmployeeDTO;
import com.bakaibank.booking.dto.employee.EmployeeDTO;
import com.bakaibank.booking.entity.Employee;
import com.bakaibank.booking.repository.EmployeeRepository;
import com.bakaibank.booking.service.EmployeeService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Optional;

@Service
@Validated
public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeRepository employeeRepository;

    private final ModelMapper modelMapper;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public EmployeeServiceImpl(EmployeeRepository employeeRepository,
                               ModelMapper modelMapper,
                               PasswordEncoder passwordEncoder) {
        this.employeeRepository = employeeRepository;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
    }
    @Override
    public List<EmployeeDTO> findAll() {
        List<Employee> employees = employeeRepository.findAllWithPositionAndTeam();
        return employees.stream()
                .map(employee -> modelMapper.map(employee, EmployeeDTO.class))
                .toList();
    }

    @Override
    public Optional<EmployeeDTO> findById(Long id) {
        return employeeRepository.findByIdWithPositionAndTeam(id)
                .map(employee -> modelMapper.map(employee, EmployeeDTO.class));
    }

    @Override
    public EmployeeDTO save(@Valid CreateEmployeeDTO createEmployeeDTO) {
        createEmployeeDTO.setPassword(passwordEncoder.encode(createEmployeeDTO.getPassword()));

        Employee employee = employeeRepository.save(modelMapper.map(createEmployeeDTO, Employee.class));
        return modelMapper.map(employee, EmployeeDTO.class);
    }

    @Override
    public void deleteById(Long id) {
        employeeRepository.deleteById(id);
    }
}
