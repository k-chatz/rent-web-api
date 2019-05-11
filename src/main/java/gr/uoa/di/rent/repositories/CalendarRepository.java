package gr.uoa.di.rent.repositories;

import gr.uoa.di.rent.models.Calendar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

public interface CalendarRepository extends JpaRepository<Calendar, Long> {

    @Transactional
    @Modifying
    @Query(value="SELECT * FROM calendar WHERE room = :roomID\n" +
            "AND ((start_date <= :startDate AND :endDate <= end_date)\n" +
            "OR (start_date <= :endDate AND :endDate <= end_date)\n" +
            "OR (:startDate <= end_date AND :endDate >= end_date))\n", nativeQuery = true)
    List<Calendar> isRoomAvailable(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate, @Param("roomID") Long roomID);
}
