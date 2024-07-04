package com.bakaibank.booking.repository;

import com.bakaibank.booking.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Optional<Employee> findByUsernameIgnoreCase(String username);
}
