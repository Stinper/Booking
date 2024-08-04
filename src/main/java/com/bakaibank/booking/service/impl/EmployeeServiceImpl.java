package com.bakaibank.booking.service.impl;

import com.bakaibank.booking.dto.employee.CreateEmployeeDTO;
import com.bakaibank.booking.dto.employee.EmployeeDTO;
import com.bakaibank.booking.dto.employee.EmployeeRolesDTO;
import com.bakaibank.booking.entity.Employee;
import com.bakaibank.booking.exceptions.ValidationException;
import com.bakaibank.booking.repository.EmployeeRepository;
import com.bakaibank.booking.repository.RoleRepository;
import com.bakaibank.booking.service.EmployeeService;
import com.bakaibank.booking.validation.EmployeeRolesValidator;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
@Validated
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final EmployeeRolesValidator employeeRolesValidator;

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
        EmployeeRolesDTO roles = new EmployeeRolesDTO(createEmployeeDTO.getRoles());
        Errors errors = new BeanPropertyBindingResult(roles, "createEmployeeRolesErrors");
        employeeRolesValidator.validate(roles, errors);

        if(errors.hasErrors())
            throw new ValidationException(errors);

        createEmployeeDTO.setPassword(passwordEncoder.encode(createEmployeeDTO.getPassword()));

        Employee employee = employeeRepository.save(modelMapper.map(createEmployeeDTO, Employee.class));
        return modelMapper.map(employee, EmployeeDTO.class);
    }

    @Override
    public void deleteById(Long id) {
        employeeRepository.deleteById(id);
    }

    @Override
    public EmployeeRolesDTO findRolesById(Long employeeId) throws ResponseStatusException {
        Employee employee = employeeRepository.findByIdWithRoles(employeeId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Сотрудник с таким ID не найден"));

        return modelMapper.map(employee, EmployeeRolesDTO.class);
    }

    @Override
    public EmployeeRolesDTO updateRoles(Long employeeId, @Valid EmployeeRolesDTO roles) {
        Employee employee = employeeRepository.findByIdWithRoles(employeeId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Сотрудник с таким ID не найден"));

        Errors errors = new BeanPropertyBindingResult(roles, "updateEmployeeRolesErrors");
        employeeRolesValidator.validate(roles, errors);

        if(errors.hasErrors())
            throw new ValidationException(errors);

        employee.setRoles(roleRepository.findByNameIn(roles.getRoles()));
        return modelMapper.map(employeeRepository.save(employee), EmployeeRolesDTO.class);
    }
}
