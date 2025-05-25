package ru.top.cinemas.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.top.cinemas.entities.Booking;
import ru.top.cinemas.entities.BookingStatus;

import java.util.List;
import java.util.Optional;


@Repository
public interface BookingRepository extends JpaRepository<Booking,Long> {

    Optional<Booking> findByIdAndUserId(Long id,Long userId);
    List<Booking> findAllByUserId(Long userId);
    List<Booking> findAllByUserIdAndAndStatus(Long userId, BookingStatus status);
    List<Booking> findAllByUserIdAndAndStatusIn(Long userId, List<BookingStatus> status);

}
