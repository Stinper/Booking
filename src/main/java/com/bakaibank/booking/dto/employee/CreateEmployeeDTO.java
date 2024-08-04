package com.bakaibank.booking.dto.employee;

import com.bakaibank.booking.entity.Employee;
import com.bakaibank.booking.entity.Position;
import com.bakaibank.booking.entity.Team;
import com.bakaibank.booking.validation.annotations.BakaiEmail;
import com.bakaibank.booking.validation.annotations.ForeignKey;
import com.bakaibank.booking.validation.annotations.Password;
import com.bakaibank.booking.validation.annotations.Unique;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateEmployeeDTO {
    @NotBlank(message = "Имя пользователя должно быть указано")
    @Unique(message = "Пользователь с таким именем уже зарегистрирован", field = "username", entity = Employee.class)
    private String username;

    @NotBlank(message = "Почта должна быть указана")
    @BakaiEmail
    @Unique(message = "Пользователь с такой почтой уже зарегистрирован", field = "email", entity = Employee.class)
    private String email;

    @NotBlank(message = "Пароль должен быть указан")
    @Password
    private String password;

    @NotBlank(message = "Имя должно быть указано")
    private String firstName;

    @NotBlank(message = "Фамилия должна быть указана")
    private String lastName;

    private String middleName;

    @ForeignKey(message = "Должность с таким ID не найдена", entity = Position.class)
    private Long positionId;

    @ForeignKey(message = "Команда с таким ID не найдена", entity = Team.class)
    private Long teamId;

    @NotNull(message = "Укажите список ролей")
    private Set<String> roles;

    public CreateEmployeeDTO(String username, String email, String password,
                             String firstName, String lastName, Set<String> roles) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.roles = roles;
    }
}
