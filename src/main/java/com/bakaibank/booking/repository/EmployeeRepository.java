package com.bakaibank.booking.repository;

import com.bakaibank.booking.entity.Employee;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    @EntityGraph(attributePaths = "roles", type = EntityGraph.EntityGraphType.LOAD)
    Optional<Employee> findByUsernameIgnoreCase(String username);

    @EntityGraph(attributePaths = {"position", "team"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query("SELECT e FROM Employee e")
    List<Employee> findAllWithPositionAndTeam();

    @EntityGraph(attributePaths = {"position", "team"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query("SELECT e FROM Employee e WHERE e.id = :id")
    Optional<Employee> findByIdWithPositionAndTeam(@Param("id") Long id);
}
