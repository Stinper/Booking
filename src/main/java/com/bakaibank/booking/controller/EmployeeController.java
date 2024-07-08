package com.bakaibank.booking.controller;

import com.bakaibank.booking.dto.CreateEmployeeDTO;
import com.bakaibank.booking.dto.EmployeeDTO;
import com.bakaibank.booking.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {
    private final EmployeeService employeeService;

    @Autowired
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping
    public List<EmployeeDTO> findAllEmployees() {
        return employeeService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeDTO> findEmployeeById(@PathVariable Long id) {
        return employeeService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EmployeeDTO createEmployee(@RequestBody CreateEmployeeDTO createEmployeeDTO) {
        return employeeService.save(createEmployeeDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEmployeeById(@PathVariable Long id) {
        return employeeService.findById(id)
                .map(employee -> {
                    employeeService.deleteById(id);
                    return ResponseEntity.ok(employee);
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
