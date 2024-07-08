package com.bakaibank.booking.service;

import com.bakaibank.booking.dto.CreateEmployeeDTO;
import com.bakaibank.booking.dto.EmployeeDTO;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Optional;

public interface EmployeeService {
    List<EmployeeDTO> findAll();
    Optional<EmployeeDTO> findById(Long id);
    EmployeeDTO save(@Valid CreateEmployeeDTO createEmployeeDTO);
    void deleteById(Long id);
}
