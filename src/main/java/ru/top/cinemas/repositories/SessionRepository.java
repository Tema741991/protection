package ru.top.cinemas.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Repository;
import ru.top.cinemas.entities.Film;
import ru.top.cinemas.entities.Hall;
import ru.top.cinemas.entities.Session;
import ru.top.cinemas.entities.SessionStatus;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;


@Repository
public interface SessionRepository extends JpaRepository<Session, Long>, JpaSpecificationExecutor<Session> {

    Page<Session> findAll(Specification<Session> spec, Pageable pageable);



    @Query("SELECT s FROM Session s WHERE s.hall.id = :hallId AND s.startTime BETWEEN :startOfDay AND :endOfDay")
    List<Session> findByHallIdAndDate(
            @Param("hallId") Long hallId,
            @Param("startOfDay") LocalDateTime startOfDay,
            @Param("endOfDay") LocalDateTime endOfDay
    );



    List<Session> findByStartTimeBetweenAndStatus(LocalDateTime startOfDay,LocalDateTime endOfDay,SessionStatus status);



    @Query("SELECT s FROM Session s WHERE s.film.id = :filmId " +
            "AND s.startTime BETWEEN :start AND :end " +
            "AND s.status = :status " +
            "ORDER BY s.startTime")
    List<Session> findByFilmIdAndDateRangeAndStatus(
            @Param("filmId") Long filmId,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end,
            @Param("status") SessionStatus status
    );


    @Query("SELECT COUNT(s) > 0 FROM Session s WHERE " +
            "s.hall = :hall AND " +
            "((s.startTime < :endDateTime) AND (s.endTime > :startDateTime))")
    boolean existsByHallAndDateTimeRange(
            @Param("hall") Hall hall,
            @Param("startDateTime") LocalDateTime startDateTime,
            @Param("endDateTime") LocalDateTime endDateTime);

    boolean existsByHallIdAndStatus(Long hallId,SessionStatus status);
    boolean existsSessionsByFilmId(Long id);
}
