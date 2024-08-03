package com.bakaibank.booking.validation;

import com.bakaibank.booking.dto.employee.EmployeeRolesDTO;
import com.bakaibank.booking.entity.Employee;
import com.bakaibank.booking.entity.Role;
import com.bakaibank.booking.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Collection;
import java.util.List;

@Component
@Setter
@RequiredArgsConstructor
public class EmployeeRolesValidator implements Validator {
    private final RoleRepository roleRepository;
    private Employee updatableEmployee;

    @Override
    public boolean supports(Class<?> clazz) {
        return EmployeeRolesDTO.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        if(updatableEmployee == null) throw new RuntimeException("updatableEmployee не может быть NULL");
        EmployeeRolesDTO dto = (EmployeeRolesDTO) target;

        if(!validateAllRolesExists(dto.getRoles(), errors)) return;
    }

    /**
     * Проверяет, что все роли, представленные в списке, существуют
     * @param roleNames Список ролей
     */
    private boolean validateAllRolesExists(Collection<String> roleNames, Errors errors) {
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
