package com.bakaibank.booking.dto.team;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateTeamDTO {
    @NotBlank(message = "Укажите название команды")
    private String name;
}
