package ru.top.cinemas.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.top.cinemas.entities.WorkTime;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Optional;

@Repository
public interface WorkTimeRepository extends JpaRepository<WorkTime,Long> {
    List<WorkTime> findByHallId(Long hallId);
    Optional<WorkTime> findByDayOfWeek(DayOfWeek dow);
    Optional<WorkTime> findByHallIdAndDayOfWeek(Long hallId,DayOfWeek day);
}
