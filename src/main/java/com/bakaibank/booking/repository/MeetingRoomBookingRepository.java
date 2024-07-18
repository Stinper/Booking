package com.bakaibank.booking.repository;

import com.bakaibank.booking.entity.MeetingRoomBooking;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface MeetingRoomBookingRepository extends JpaRepository<MeetingRoomBooking, Long> {
    @EntityGraph(value = "MeetingRoomBooking.withMeetingRoomInfoAndFullEmployeeInfo", type = EntityGraph.EntityGraphType.LOAD)
    List<MeetingRoomBooking> findAllByDate(LocalDate date);

    @EntityGraph(value = "MeetingRoomBooking.withMeetingRoomInfoAndFullEmployeeInfo", type = EntityGraph.EntityGraphType.LOAD)
    List<MeetingRoomBooking> findAllByMeetingRoom_IdAndDate(Long meetingRoomId, LocalDate date);

    //Пересекается ли встреча в комнате {roomId} с заданными {startTime} и {endTime}
    // с другими встречами в эту же дату в этой комнате
    @Query("SELECT COUNT(mrb.id) > 0 " +
            "FROM MeetingRoomBooking mrb " +
            "JOIN mrb.meetingRoom room " +
            "WHERE room.id = :roomId " +
            "AND mrb.date = :date " +
            "AND ((mrb.startTime BETWEEN :startTime AND :endTime) OR (mrb.endTime BETWEEN :startTime AND :endTime))")
    boolean isMeetingIntersectsWithOtherMeetings(@Param("roomId") Long roomId,
                                                 @Param("date") LocalDate date,
                                                 @Param("startTime") LocalTime startTime,
                                                 @Param("endTime") LocalTime endTime);

    // Выбирает сотрудника, у которого в заданную дату уже есть
    // запланированная встреча, которая пересекается с текущей встречей
    @Query("SELECT p.username " +
            "FROM MeetingRoomBooking mrb " +
            "JOIN mrb.participants p " +
            "WHERE p.username IN :participantUsernames " +
            "AND mrb.date = :date " +
            "AND ((:startTime BETWEEN mrb.startTime AND mrb.endTime) " +
            "OR (:endTime BETWEEN mrb.startTime AND mrb.endTime) " +
            "OR (mrb.startTime BETWEEN :startTime AND :endTime))")
    Optional<String> findEmployeeWithConflictBooking(@Param("participantUsernames") Collection<String> participantUsernames,
                                                     @Param("date") LocalDate date,
                                                     @Param("startTime") LocalTime startTime,
                                                     @Param("endTime") LocalTime endTime);
}
