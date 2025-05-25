package ru.top.cinemas.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.top.cinemas.entities.Hall;
import ru.top.cinemas.entities.Seat;

import java.util.List;
import java.util.Optional;


@Repository
public interface SeatRepository extends JpaRepository<Seat,Long> {

    List<Seat> findSeatsByHallId(Long id);

    Seat findSeatByIdAndHallId(Long seadId,Long hallId);

    int countByHallId(Long hallId);

    int countByHallIdAndActiveSeatFalse(Long hallId);

    List<Seat> findByHallIdOrderByRowNumberAscSeatNumberAsc(Long hallId);

    boolean existsByHallIdAndRowNumberAndSeatNumber(Long id, String rowNumber, String seatNumber);

    void deleteAllByHallId(Long hallId);

    Optional<Seat> findByHallAndRowNumberAndSeatNumber(Hall hall, String s, String s1);
}
