package com.bakaibank.booking.dto.employee.converters;

import com.bakaibank.booking.dto.employee.CreateEmployeeDTO;
import com.bakaibank.booking.dto.employee.EmployeeDTO;
import com.bakaibank.booking.dto.employee.EmployeeRolesDTO;
import com.bakaibank.booking.dto.position.PositionDTO;
import com.bakaibank.booking.dto.team.TeamDTO;
import com.bakaibank.booking.entity.Employee;
import com.bakaibank.booking.entity.Position;
import com.bakaibank.booking.entity.Role;
import com.bakaibank.booking.entity.Team;
import com.bakaibank.booking.repository.PositionRepository;
import com.bakaibank.booking.repository.RoleRepository;
import com.bakaibank.booking.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class EmployeeConvertersManager {
    private final PositionRepository positionRepository;
    private final TeamRepository teamRepository;
    private final RoleRepository roleRepository;

    public static Converter<Employee, EmployeeDTO> employeeToEmployeeDTOConverter() {
        return new Converter<>() {
            @Override
            public EmployeeDTO convert(MappingContext<Employee, EmployeeDTO> mappingContext) {
                return mapEmployeeToEmployeeDTO(mappingContext.getSource());
            }
        };
    }

    public static EmployeeDTO mapEmployeeToEmployeeDTO(Employee employee) {
        return new EmployeeDTO(
                employee.getId(),
                employee.getUsername(),
                employee.getFirstName(),
                employee.getLastName(),
                employee.getMiddleName(),
                Optional.ofNullable(employee.getPosition())
                        .map(position -> new PositionDTO(position.getId(), position.getName()))
                        .orElse(null),
                Optional.ofNullable(employee.getTeam())
                        .map(team -> new TeamDTO(team.getId(), team.getName()))
                        .orElse(null)
        );
    }

    public Converter<CreateEmployeeDTO, Employee> createEmployeeDTOToEmployeeConverter() {
        return new Converter<>() {
            @Override
            public Employee convert(MappingContext<CreateEmployeeDTO, Employee> mappingContext) {
                CreateEmployeeDTO employeeDTO = mappingContext.getSource();
                Position position = null; Team team = null;

                if (employeeDTO.getPositionId() != null)
                    position = positionRepository.findById(employeeDTO.getPositionId()).orElse(null);

                if (employeeDTO.getTeamId() != null)
                    team = teamRepository.findById(employeeDTO.getTeamId()).orElse(null);

                List<Role> roles = new ArrayList<>();

                if (!employeeDTO.getRoles().isEmpty())
                    roles = roleRepository.findByNameIn(employeeDTO.getRoles());

                return new Employee(
                        employeeDTO.getUsername(),
                        employeeDTO.getEmail(),
                        employeeDTO.getPassword(),
                        employeeDTO.getFirstName(),
                        employeeDTO.getLastName(),
                        employeeDTO.getMiddleName(),
                        position,
                        team,
                        roles
                );
            }
        };
    }

    public static Converter<Employee, EmployeeRolesDTO> employeeToEmployeeRolesDTOConverter() {
        return new Converter<>() {
            @Override
            public EmployeeRolesDTO convert(MappingContext<Employee, EmployeeRolesDTO> mappingContext) {
                Employee employee = mappingContext.getSource();

                return new EmployeeRolesDTO(
                        employee.getRoles().stream()
                                .map(Role::getName)
                                .collect(Collectors.toSet())
                );
            }
        };
    }
}
