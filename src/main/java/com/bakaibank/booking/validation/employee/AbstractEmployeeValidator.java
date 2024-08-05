package com.bakaibank.booking.validation.employee;

import com.bakaibank.booking.dto.employee.AbstractEmployeeDTO;
import com.bakaibank.booking.entity.Role;
import com.bakaibank.booking.repository.EmployeeRepository;
import com.bakaibank.booking.repository.RoleRepository;
import lombok.NoArgsConstructor;
import org.springframework.validation.Errors;

import java.util.Collection;
import java.util.List;

@NoArgsConstructor
public abstract class AbstractEmployeeValidator {
    protected EmployeeRepository employeeRepository;
    protected RoleRepository roleRepository;

    public AbstractEmployeeValidator(EmployeeRepository employeeRepository,
                                     RoleRepository roleRepository) {
        this.employeeRepository = employeeRepository;
        this.roleRepository = roleRepository;
    }

    protected void validate(AbstractEmployeeDTO dto, Errors errors) {
        if (!validateAllRolesExists(dto.getRoles(), errors)) return;
    }

    protected boolean validateUsername(String username, Errors errors) {
        if (employeeRepository.existsByUsernameIgnoreCase(username)) {
            errors.reject("usernameAlreadyExists",
                    "Сотрудник с таким именем пользователя уже существует");
            return false;
        }

        return true;
    }
    protected boolean validateEmail(String email, Errors errors) {
        if(employeeRepository.existsByEmailIgnoreCase(email)) {
            errors.reject("emailAlreadyExists", "Сотрудник с такой почтой уже зарегистрирован");
            return false;
        }

        return true;
    }

    public boolean validateAllRolesExists(Collection<String> roleNames, Errors errors) {
        List<Role> roles = roleRepository.findByNameIn(roleNames);

        for(String roleName : roleNames) {
            if(roles.stream().noneMatch(role -> role.getName().equalsIgnoreCase(roleName))) {
                errors.reject("roleDoesNotExists", "Роль с названием " + roleName + " не существует");
                return false;
            }
        }

        return true;
    }
}
