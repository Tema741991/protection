package ru.top.cinemas.services.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.top.cinemas.entities.*;
import ru.top.cinemas.repositories.BookingRepository;
import ru.top.cinemas.repositories.SeatSessionRepository;
import ru.top.cinemas.repositories.SessionRepository;
import ru.top.cinemas.repositories.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl {

    private final SeatSessionRepository seatSessionRepository;
    private final BookingRepository bookingRepository;
    private final SessionRepository sessionRepository;
    private final UserRepository userRepository;


    @Transactional
    public Booking reserveSeat(User user,Long sessionId, Long seatId) {
        SeatSession seatSession = seatSessionRepository.findById(seatId)
                .orElseThrow(() -> new EntityNotFoundException("Место не найдено"));

        if (seatSession.getStatus() == SeatStatus.INACTIVE) {
            throw new IllegalStateException("Место неактивно");
        }
        if (seatSession.getStatus() == SeatStatus.SOLD) {
            throw new IllegalStateException("Место уже продано");
        }
        if (seatSession.getStatus() == SeatStatus.RESERVED) {
            throw new IllegalStateException("Место уже забронировано");
        }
        seatSession.setStatus(SeatStatus.RESERVED);
        seatSessionRepository.save(seatSession);
        Booking booking = new Booking();
        booking.setUser(user);
        booking.setSession(seatSession.getSession());
        booking.setSeat(seatSession.getSeatTemplate());
        booking.setPrice(seatSession.getPrice());
        booking.setBookingTime(LocalDateTime.now());
        booking.setStatus(BookingStatus.RESERVED);
        bookingRepository.save(booking);
        return booking;
    }

    @Transactional
    public void buySeatLater(Long bookingId, Long sessionId, Long seatId) {
        SeatSession seatSession = seatSessionRepository.findBySessionIdAndSeatTemplateId(sessionId,seatId)
                .orElseThrow(() -> new EntityNotFoundException("Место не найдено"));
        if (seatSession.getStatus() != SeatStatus.RESERVED) {
            throw new IllegalStateException("Можно купить только бронирования");
        }
        Booking booking = bookingRepository.findById(bookingId). orElseThrow(() -> new EntityNotFoundException("Бронирование не найдено"));
        seatSession.setStatus(SeatStatus.SOLD);
        seatSessionRepository.save(seatSession);
        booking.setBookingTime(LocalDateTime.now());
        booking.setStatus(BookingStatus.SOLD);
        bookingRepository.save(booking);
    }

    @Transactional
    public void cancelReservation(Long bookingId,Long sessionId, Long seatId) {
        SeatSession seatSession = seatSessionRepository.findBySessionIdAndSeatTemplateId(sessionId,seatId)
                .orElseThrow(() -> new EntityNotFoundException("Место не найдено"));
        if (seatSession.getStatus() != SeatStatus.RESERVED) {
            throw new IllegalStateException("Можно вернуть только билеты со статусом RESERVED");
        }
        Booking booking = bookingRepository.findById(bookingId). orElseThrow(() -> new EntityNotFoundException("Бронирование не найдено"));
        seatSession.setStatus(SeatStatus.ACTIVE);
        seatSessionRepository.save(seatSession);
        booking.setBookingTime(LocalDateTime.now());
        booking.setStatus(BookingStatus.CANCELLED);
        bookingRepository.save(booking);
    }

    @Transactional
    public void cancelSold(Long bookingId,Long sessionId, Long seatId) {
        SeatSession seatSession = seatSessionRepository.findBySessionIdAndSeatTemplateId(sessionId,seatId)
                .orElseThrow(() -> new EntityNotFoundException("Место не найдено"));

        if (seatSession.getStatus() != SeatStatus.SOLD) {
            throw new IllegalStateException("Можно вернуть только билеты со статусом SOLD");
        }
        Booking booking = bookingRepository.findById(bookingId). orElseThrow(() -> new EntityNotFoundException("Бронирование не найдено"));
        seatSession.setStatus(SeatStatus.ACTIVE);
        seatSessionRepository.save(seatSession);
        booking.setBookingTime(LocalDateTime.now());
        booking.setStatus(BookingStatus.RETURNED);
        bookingRepository.save(booking);
    }

    @Transactional(readOnly = true)
    public List<Booking> getUserBookings(Long userId) {
        User findById = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("Пользователь не найден"));
        List<Booking> allBookingByUserId = bookingRepository.findAllByUserId(findById.getId());
    return allBookingByUserId;
    }

    @Transactional(readOnly = true)
    public List<Booking> findAllByUserIdAndAndStatus(Long userId,BookingStatus status) {
        User findById = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("Пользователь не найден"));
        List<Booking> allBookingByUserIdAndStatus = bookingRepository.findAllByUserIdAndAndStatus(findById.getId(), status);
        return allBookingByUserIdAndStatus;
    }

    @Transactional(readOnly = true)
    public List<Booking> findAllByUserIdAndAndStatusIn(Long userId,List<BookingStatus> statuses) {
        User findById = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("Пользователь не найден"));
        List<Booking> allBookingByUserIdAndStatus = bookingRepository.findAllByUserIdAndAndStatusIn(findById.getId(), statuses);
        return allBookingByUserIdAndStatus;
    }

    @Transactional(readOnly = true)
    public SeatSession findBySessionIdAndSeatTemplateId(Long sessionId, Long seatId){
        return seatSessionRepository.findBySessionIdAndSeatTemplateId(sessionId,seatId)
                .orElseThrow(() -> new EntityNotFoundException("Место не найдено"));
    }

    @Transactional(readOnly = true)
    public Booking viewTicket(Long bookingId){
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() -> new EntityNotFoundException("Бронирование не найдено"));
        return booking;
    }
}
