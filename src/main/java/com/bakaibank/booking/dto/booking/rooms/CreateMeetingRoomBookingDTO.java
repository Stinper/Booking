package com.bakaibank.booking.dto.booking.rooms;

import com.bakaibank.booking.entity.MeetingRoom;
import com.bakaibank.booking.validation.annotations.ForeignKey;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateMeetingRoomBookingDTO {
    @Null
    private Long employeeId;

    @NotNull(message = "Укажите переговорную")
    @ForeignKey(message = "Переговорная комната с таким ID не найдена", entity = MeetingRoom.class)
    private Long meetingRoomId;

    @NotBlank(message = "Укажите тему собрания")
    private String topic;

    @NotNull(message = "Укажите дату собрания")
    private LocalDate date;

    @NotNull(message = "Укажите время начала собрания")
    private LocalTime startTime;

    @NotNull(message = "Укажите время конца собрания")
    private LocalTime endTime;

    @NotNull(message = "Выберите участников собрания")
    private Set<String> participants;
}
