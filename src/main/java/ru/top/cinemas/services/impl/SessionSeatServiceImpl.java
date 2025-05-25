package ru.top.cinemas.services.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.top.cinemas.entities.SeatSession;
import ru.top.cinemas.entities.SeatStatus;
import ru.top.cinemas.entities.Session;
import ru.top.cinemas.repositories.SeatSessionRepository;
import ru.top.cinemas.repositories.SessionRepository;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SessionSeatServiceImpl {

    private final SeatSessionRepository seatSessionRepository;
    private final SessionRepository sessionRepository;

    @Transactional
    public void reserveSeat(Long seatId) {
        SeatSession seat = seatSessionRepository.findById(seatId)
                .orElseThrow(() -> new EntityNotFoundException("Место не найдено"));

        if (seat.getStatus() == SeatStatus.ACTIVE) {
            seat.setStatus(SeatStatus.RESERVED);
            seatSessionRepository.save(seat);
        } else {
            throw new IllegalStateException("Невозможно забронировать место с текущим статусом: " + seat.getStatus());
        }
    }

    @Transactional
    public void buySeat(Long seatId) {
        SeatSession seat = seatSessionRepository.findById(seatId)
                .orElseThrow(() -> new EntityNotFoundException("Место не найдено"));

        if (seat.getStatus() == SeatStatus.ACTIVE || seat.getStatus() == SeatStatus.RESERVED) {
            seat.setStatus(SeatStatus.SOLD);
            seatSessionRepository.save(seat);
        } else {
            throw new IllegalStateException("Невозможно купить место с текущим статусом: " + seat.getStatus());
        }
    }

    @Transactional
    public void toggleActive(Long seatId) {
        SeatSession seat = seatSessionRepository.findById(seatId)
                .orElseThrow(() -> new EntityNotFoundException("Место не найдено"));

        if (seat.getStatus() == SeatStatus.INACTIVE) {
            // Активируем место (по умолчанию становится ACTIVE)
            seat.setStatus(SeatStatus.ACTIVE);
        } else {
            // Деактивируем место (независимо от текущего статуса)
            seat.setStatus(SeatStatus.INACTIVE);
        }
        seatSessionRepository.save(seat);
    }

    @Transactional(readOnly = true)
    public Map<String, Long> getSeatStatsForSession(Long sessionId) {
        // Получаем статистику из базы
        Map<String, Long> stats = seatSessionRepository.findBySessionId(sessionId)
                .stream()
                .collect(Collectors.groupingBy(
                        seat -> seat.getStatus().name().toLowerCase(),
                        Collectors.counting()
                ));

        // Инициализируем карту со всеми возможными статусами
        Map<String, Long> result = new LinkedHashMap<>();

        // Добавляем все возможные статусы с дефолтным значением 0
        for (SeatStatus status : SeatStatus.values()) {
            result.put(status.name().toLowerCase(), stats.getOrDefault(status.name().toLowerCase(), 0L));
        }

        return result;
    }
    @Transactional(readOnly = true)
    public List<SeatSession> getSeatsBySessionId(Long sessionId){
        Session session = sessionRepository.findById(sessionId).orElseThrow(() -> new RuntimeException("Сеанс не найден"));
        return session.getSeatSessions();
    }
}
