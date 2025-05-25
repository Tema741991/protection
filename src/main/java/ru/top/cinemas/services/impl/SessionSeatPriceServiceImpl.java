package ru.top.cinemas.services.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.top.cinemas.entities.SeatSession;
import ru.top.cinemas.repositories.SeatSessionRepository;
import ru.top.cinemas.services.SessionSeatPriceService;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class SessionSeatPriceServiceImpl implements SessionSeatPriceService {
    private final SeatSessionRepository seatSessionRepository;

    @Override
    public void setBasePriceForSession(Long sessionId, BigDecimal price) {
        List<SeatSession> seats = seatSessionRepository.findBySessionId(sessionId);
        seats.forEach(seat -> seat.setPrice(price));
        seatSessionRepository.saveAll(seats);
    }


    @Override
    public void setCustomPrice(Long seatSessionId, BigDecimal price) {
        SeatSession seat = seatSessionRepository.findById(seatSessionId)
                .orElseThrow(() -> new EntityNotFoundException("Место не найдено"));
        seat.setPrice(price);
        seatSessionRepository.save(seat);
    }

    @Override
    public void setPriceForSeat(Long seatId, BigDecimal price) {
        SeatSession seat = seatSessionRepository.findById(seatId)
                .orElseThrow(() -> new EntityNotFoundException("Место не найдено"));
        seat.setPrice(price);
        seatSessionRepository.save(seat);
    }

    @Override
    public void setPriceForMultipleSeats(List<Long> seatIds, BigDecimal price) {
        List<SeatSession> seats = seatSessionRepository.findAllById(seatIds);
        if (seats.size() != seatIds.size()) {
            throw new EntityNotFoundException("Некоторые места не найдены");
        }
        seats.forEach(seat -> {
            seat.setPrice(price);
            seatSessionRepository.save(seat);
        });
    }

}
