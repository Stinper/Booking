package com.bakaibank.booking.dto;

import com.bakaibank.booking.entity.Employee;
import com.bakaibank.booking.validation.annotations.Password;
import com.bakaibank.booking.validation.annotations.Unique;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateEmployeeDTO {
    @NotBlank(message = "Имя пользователя должно быть указано")
    @Unique(message = "Пользователь с таким именем уже зарегистрирован", field = "username", entity = Employee.class)
    private String username;

    @NotBlank(message = "Почта должна быть указана")
    @Email(message = "Почта должна быть корректной")
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
}
