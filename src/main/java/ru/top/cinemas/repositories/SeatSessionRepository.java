package ru.top.cinemas.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.top.cinemas.entities.SeatSession;
import ru.top.cinemas.entities.Session;

import java.util.List;
import java.util.Optional;

@Repository
public interface SeatSessionRepository extends JpaRepository<SeatSession,Long> {

    void deleteAllBySession(Session session);

    List<SeatSession> findAllBySessionId(Long id);


    List<SeatSession> findBySessionId(Long sessionId);

   Optional<SeatSession> findBySessionIdAndSeatTemplateId(Long sessionId, Long seatId);
}
