package com.bakaibank.booking.validation;

import com.bakaibank.booking.dto.booking.rooms.CreateMeetingRoomBookingDTO;
import com.bakaibank.booking.repository.EmployeeRepository;
import com.bakaibank.booking.repository.MeetingRoomBookingRepository;
import com.bakaibank.booking.repository.WeekendRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.List;

@Component
public class MeetingRoomBookingValidator extends AbstractBookingValidator implements Validator {
    private final MeetingRoomBookingRepository meetingRoomBookingRepository;

    private final EmployeeRepository employeeRepository;

    @Value("${booking.rooms.allowed.days-ahead}")
    private int roomBookingAllowedDaysAhead;

    @Value("${booking.rooms.allowed.start-time}")
    private String bookingStartTimePattern;

    @Value("${booking.rooms.allowed.end-time}")
    private String bookingEndTimePattern;

    @Value("${booking.rooms.allowed.min-participants}")
    private int minParticipantsCount;

    @Value("${booking.rooms.allowed.max-participants}")
    private int maxParticipantsCount;

    @Value("${booking.rooms.allowed.min-meeting-duration-minutes}")
    private int minMeetingDurationMinutes;

    @Autowired
    public MeetingRoomBookingValidator(WeekendRepository weekendRepository,
                                       MeetingRoomBookingRepository meetingRoomBookingRepository,
                                       EmployeeRepository employeeRepository) {
        super(weekendRepository);
        this.meetingRoomBookingRepository = meetingRoomBookingRepository;
        this.employeeRepository = employeeRepository;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return CreateMeetingRoomBookingDTO.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        CreateMeetingRoomBookingDTO createMeetingRoomBookingDTO = (CreateMeetingRoomBookingDTO) target;
        LocalDate bookingDate = createMeetingRoomBookingDTO.getDate();

        if(!validateBookingOpenCloseTime(errors)) return;

        if(!validateBookingDate(bookingDate, roomBookingAllowedDaysAhead, errors)) return;

        if(!validateMeetingTime(
                createMeetingRoomBookingDTO.getStartTime(),
                createMeetingRoomBookingDTO.getEndTime(),
                errors)
        ) return;

        if(!validateParticipantsList(createMeetingRoomBookingDTO.getParticipants(), errors)) return;

        if(meetingRoomBookingRepository.isMeetingIntersectsWithOtherMeetings(
                createMeetingRoomBookingDTO.getMeetingRoomId(),
                bookingDate,
                createMeetingRoomBookingDTO.getStartTime(),
                createMeetingRoomBookingDTO.getEndTime())) {
            errors.reject("meetingIntersectsWithOtherMeetings", "Встреча пересекается с другими встречами");
            return;
        }

        meetingRoomBookingRepository.findEmployeeWithConflictBooking(
                createMeetingRoomBookingDTO.getParticipants(),
                bookingDate,
                createMeetingRoomBookingDTO.getStartTime(),
                createMeetingRoomBookingDTO.getEndTime()
        ).ifPresent(participantWithConflictBooking ->
                errors.reject("oneOfParticipantsHasConflictBookings", "Участник с ником "
                        + participantWithConflictBooking + " уже участвует во встрече, которая пересекается по" +
                        " времени с планируемой встречей"));

    }

    private boolean validateMeetingTime(LocalTime startTime, LocalTime endTime, Errors errors) {
        LocalTime bookingStartTime = parseTimeFromPattern(this.bookingStartTimePattern);
        LocalTime bookingEndTime = parseTimeFromPattern(this.bookingEndTimePattern);

        if(startTime.isBefore(bookingStartTime)) {
            errors.reject("invalidRoomBookingTime",
                    "Встреча в переговорной комнате должна начинаться с " + bookingStartTime);
            return false;
        }

        if(endTime.isAfter(bookingEndTime)) {
            errors.reject("invalidRoomBookingTime",
                    "Встреча в переговорной комнате должна заканчивается в " + bookingEndTime);

            return false;
        }

        if(ChronoUnit.MINUTES.between(startTime, endTime) < minMeetingDurationMinutes) {
            errors.reject("meetingIsTooShort",
                    "Минимальное время встречи (в минутах): " + minMeetingDurationMinutes);
            return false;
        }

        return true;
    }

    private boolean validateParticipantsList(Collection<String> participants, Errors errors) {
        if(participants.size() < minParticipantsCount) {
            errors.reject("notEnoughParticipants",
                    "Минимальное количество участников встречи: " + minParticipantsCount);
            return false;
        }

        if(participants.size() > maxParticipantsCount) {
            errors.reject("tooMuchParticipants",
                    "Максимальное количество участников встречи: " + maxParticipantsCount);
            return false;
        }

        List<String> existingUsernames = employeeRepository.findExistingUsernames(participants);

        for (String username : participants) {
            if (!existingUsernames.contains(username)) {
                errors.reject("invalidParticipant", "Сотрудника с ником " + username + " не существует");
                return false;
            }
        }

        return true;
    }
}
