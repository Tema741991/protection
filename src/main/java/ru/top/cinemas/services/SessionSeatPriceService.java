package ru.top.cinemas.services;

import java.math.BigDecimal;
import java.util.List;

public interface SessionSeatPriceService {
    void setBasePriceForSession(Long sessionId, BigDecimal price);
    void setCustomPrice(Long seatSessionId, BigDecimal price);
    void setPriceForSeat(Long seatId, BigDecimal price);
    void setPriceForMultipleSeats(List<Long> seatIds, BigDecimal price);
}
