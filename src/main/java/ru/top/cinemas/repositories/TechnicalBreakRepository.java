package ru.top.cinemas.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.top.cinemas.entities.Hall;
import ru.top.cinemas.entities.Session;
import ru.top.cinemas.entities.TechnicalBreak;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collection;
import java.util.List;

@Repository
public interface TechnicalBreakRepository extends JpaRepository<TechnicalBreak,Long>, JpaSpecificationExecutor<TechnicalBreak> {

    Page<TechnicalBreak> findAll(Specification<TechnicalBreak> spec, Pageable pageable);

    List<TechnicalBreak> findAllByHallIdAndDate(Long id, LocalDate currentDate);


    @Query("SELECT COUNT(t) > 0 FROM TechnicalBreak t WHERE " +
            "t.hall.id = :hallId AND t.date = :date AND " +
            "((CAST(t.start AS java.time.LocalTime) <= CAST(:end AS java.time.LocalTime) " +
            "AND CAST(t.end AS java.time.LocalTime) >= CAST(:start AS java.time.LocalTime)))")
    boolean existsTechnicalBreakDuringInterval(
            @Param("hallId") Long hallId,
            @Param("date") LocalDate date,
            @Param("start") LocalTime start,
            @Param("end") LocalTime end);


    List<TechnicalBreak> findByHallIdAndDate(Long id, LocalDate date);
}

