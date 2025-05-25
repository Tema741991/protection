package ru.top.cinemas.services.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.top.cinemas.dtos.Schedule.*;
import ru.top.cinemas.dtos.TechnicalBreakDto;
import ru.top.cinemas.entities.*;
import ru.top.cinemas.repositories.*;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ScheduleServiceImpl {
    private final HallRepository hallRepository;
    private final FilmRepository filmRepository;
    private final SessionRepository sessionRepository;
    private final TechnicalBreakRepository technicalBreakRepository;
    private final WorkTimeRepository workTimeRepository;
    private final SeatSessionRepository seatSessionRepository;
    private final SeatRepository seatRepository;

    @Value("${session.slot-break-minutes}")
    private int cleaningDuration;

    @Value("${cinema.default-price}")
    private BigDecimal price;


    public TimeGridResponse generateTimeGrid(Long hallId, LocalDate date, int stepMinutes) {

        if (stepMinutes % 5 != 0) {
            throw new IllegalArgumentException("Step must be a multiple of 5 minutes");
        }

        Hall hall = hallRepository.findById(hallId)
                .orElseThrow(() -> new IllegalArgumentException("Hall not found"));

        WorkTime workTime = workTimeRepository.findByHallIdAndDayOfWeek(hall.getId(), date.getDayOfWeek())
                .orElseThrow(() -> new IllegalArgumentException("Hall is not working this day"));

        List<TimeSlotDto> slots = generateTimeSlots(workTime.getOpenTime(), workTime.getCloseTime(), stepMinutes);

        List<TechnicalBreak> breaks = technicalBreakRepository.findAllByHallIdAndDate(hall.getId(), date);
        List<Session> sessions = sessionRepository.findByHallIdAndDate(
                hall.getId(),
                date.atStartOfDay(),
                date.plusDays(1).atStartOfDay()
        );

        markUnavailableSlots(slots, sessions, breaks);

        TimeGridResponse response = new TimeGridResponse();
        response.setSlots(slots);
        response.setSessions(mapToSessionDtos(sessions));
        response.setBreaks(mapToBreakDtos(breaks));
        response.setHallName(hall.getName());
        response.setStepMinutes(stepMinutes);
        return response;
    }


    private List<TimeSlotDto> generateTimeSlots(LocalTime start, LocalTime end, int step) {
        List<TimeSlotDto> slots = new ArrayList<>();
        LocalTime current = start;

        while (current.isBefore(end)) {
            LocalTime next = current.plusMinutes(step);
            if (next.isAfter(end)) next = end;

            TimeSlotDto slot = new TimeSlotDto();
            slot.setStartTime(current);
            slot.setEndTime(next);
            slot.setStatus(TimeSlotStatus.AVAILABLE);
            slots.add(slot);
            current = next;
        }
        return slots;
    }

    private void markUnavailableSlots(List<TimeSlotDto> slots, List<Session> sessions,
                                      List<TechnicalBreak> breaks) {
        // Помечаем технические перерывы
        breaks.forEach(br -> {
            slots.forEach(slot -> {
                if (isTimeOverlap(slot.getStartTime(), slot.getEndTime(), br.getStart(), br.getEnd())) {
                    slot.setStatus(TimeSlotStatus.UNAVAILABLE_BREAK);
                }
            });
        });

        // Помечаем занятые сеансы (с учетом времени уборки)
        sessions.forEach(session -> {
            LocalTime sessionStart = session.getStartTime().toLocalTime();
            LocalTime sessionEnd = session.getEndTime().toLocalTime();
            LocalTime cleaningEnd = sessionEnd.plusMinutes(cleaningDuration);


            slots.forEach(slot -> {
                if (isTimeOverlap(slot.getStartTime(), slot.getEndTime(), sessionStart, sessionEnd)) {
                    slot.setStatus(TimeSlotStatus.UNAVAILABLE_SESSION);
                } else if (isTimeOverlap(slot.getStartTime(), slot.getEndTime(), sessionEnd, cleaningEnd)) {
                    slot.setStatus(TimeSlotStatus.UNAVAILABLE_CLEAN);
                }
            });
        });
    }

    private boolean isTimeOverlap(LocalTime start1, LocalTime end1, LocalTime start2, LocalTime end2) {
        return start1.isBefore(end2) && end1.isAfter(start2);
    }

    private List<SessionDto> mapToSessionDtos(List<Session> sessions) {
        return sessions.stream().map(s -> {
            SessionDto dto = new SessionDto();
            dto.setId(s.getId());
            dto.setStartTime(s.getStartTime());
            dto.setEndTime(s.getEndTime());
            dto.setStatus(s.getStatus());

            FilmSessionsDto filmDto = new FilmSessionsDto();
            filmDto.setId(s.getFilm().getId());
            filmDto.setTitle(s.getFilm().getTitle());
            filmDto.setDuration(s.getFilm().getDuration());
            filmDto.setPosterPath(s.getFilm().getPosterPath());
            dto.setFilm(filmDto);
            return dto;
        }).collect(Collectors.toList());
    }

    public SessionDto mapToSessionDtos(Session sessions) {
        SessionDto dto = new SessionDto();
        dto.setId(sessions.getId());
        dto.setStartTime(sessions.getStartTime());
        dto.setEndTime(sessions.getEndTime());
        dto.setStatus(sessions.getStatus());

        FilmSessionsDto filmDto = new FilmSessionsDto();
        filmDto.setId(sessions.getFilm().getId());
        filmDto.setTitle(sessions.getFilm().getTitle());
        filmDto.setDuration(sessions.getFilm().getDuration());
        filmDto.setPosterPath(sessions.getFilm().getPosterPath());
        dto.setFilm(filmDto);
        return dto;

    }
    private List<TechnicalBreakDto> mapToBreakDtos(List<TechnicalBreak> breaks) {
            return  breaks.stream().map(b ->{
                TechnicalBreakDto dto = new TechnicalBreakDto();
                dto.setId(b.getId());
                dto.setHallId(b.getHall().getId());
                dto.setStart(b.getStart());
                dto.setEnd(b.getEnd());
                dto.setStartTime(b.getDate());
                return dto;
            }).collect(Collectors.toList());
    }


    @Transactional
    public Session createSession(Long filmId, Long hallId, LocalDateTime startTime) {
        // 1. Получаем необходимые сущности из базы
        Film film = filmRepository.findById(filmId)
                .orElseThrow(() -> new IllegalArgumentException("Фильм не найден"));

        Hall hall = hallRepository.findById(hallId)
                .orElseThrow(() -> new IllegalArgumentException("Зал не найден"));

        // 2. Рассчитываем время окончания сеанса

        LocalDateTime endTime = startTime.plusMinutes(roundMinutesUp(film.getDuration()));

        // 3. Проверяем доступность зала в это время
        validateSessionTime(hall, startTime, endTime);

        // 4. Создаем сеанс
        Session session = new Session();
        session.setStartTime(startTime);
        session.setEndTime(endTime);
        session.setFilm(film);
        session.setHall(hall);
        session.setStatus(SessionStatus.DRAFT);

        // 5. Сохраняем сеанс
        Session savedSession = sessionRepository.save(session);

        // 6. Создаем места для сеанса
        createSeatSessions(savedSession, hall);

        return savedSession;
    }

    private void validateSessionTime(Hall hall, LocalDateTime startTime, LocalDateTime endTime) {
        // 1. Проверяем рабочие часы зала
        DayOfWeek dayOfWeek = startTime.getDayOfWeek();
        WorkTime workTime = workTimeRepository.findByHallIdAndDayOfWeek(hall.getId(), dayOfWeek)
                .orElseThrow(() -> new IllegalStateException("Зал не работает в этот день"));

        if (startTime.toLocalTime().isBefore(workTime.getOpenTime()) ||
                endTime.toLocalTime().isAfter(workTime.getCloseTime())) {
            throw new IllegalStateException("Сеанс выходит за пределы рабочего дня");
        }

        // 2. Проверяем технические перерывы
        List<TechnicalBreak> breaks = technicalBreakRepository.findAllByHallIdAndDate(
                hall.getId(),
                startTime.toLocalDate()
        );

        for (TechnicalBreak br : breaks) {
            if (isTimeOverlap(
                    startTime.toLocalTime(), endTime.toLocalTime(),
                    br.getStart(), br.getEnd())
            ) {
                throw new IllegalStateException("Сеанс пересекается с техническим перерывом");
            }
        }

        // 3. Проверяем другие сеансы (с учетом времени уборки)
        List<Session> sessions = sessionRepository.findByHallIdAndDate(
                hall.getId(),
                startTime.toLocalDate().atStartOfDay(),
                startTime.toLocalDate().plusDays(1).atStartOfDay()
        );

        for (Session s : sessions) {
            LocalDateTime otherSessionEnd = s.getEndTime().plusMinutes(cleaningDuration);

            if (isTimeOverlap(
                    startTime, endTime,
                    s.getStartTime(), otherSessionEnd)
            ) {
                throw new IllegalStateException("Сеанс пересекается с другими сеансами");
            }
        }
    }

    private boolean isTimeOverlap(LocalDateTime start1, LocalDateTime end1,
                                  LocalDateTime start2, LocalDateTime end2) {
        return start1.isBefore(end2) && end1.isAfter(start2);
    }

    @Transactional
    protected void createSeatSessions(Session session, Hall hall) {
        List<Seat> seats = seatRepository.findSeatsByHallId(hall.getId());
        List<SeatSession> seatSessions = new ArrayList<>();

        for (Seat seat : seats) {
            SeatStatus status;
            if(seat.isActiveSeat()){
                status=SeatStatus.ACTIVE;
            }else {
                status=SeatStatus.INACTIVE;
            }
            SeatSession seatSession = new SeatSession();
            seatSession.setSession(session);
            seatSession.setSeatTemplate(seat);
            seatSession.setPrice(price);
            seatSession.setStatus(status);
            seatSessions.add(seatSession);
        }

        seatSessionRepository.saveAll(seatSessions);
        session.setSeatSessions(seatSessions);
    }

    public void deleteSession(Long sessionId) {
        Session findSession = sessionRepository.findById(sessionId).orElseThrow(() -> new EntityNotFoundException("Сеанс не найден"));
        if (findSession.getStatus()!=SessionStatus.DRAFT){
            throw new IllegalArgumentException("Удалять сеансы можно только со статусом черновик");
        }
        sessionRepository.deleteById(findSession.getId());
    }

    private  int roundMinutesUp(int minutes) {
        if (minutes < 0) {
            throw new IllegalArgumentException("Минуты не могут быть отрицательными");
        }

        // Вычисляем остаток от деления на 5
        int remainder = minutes % 5;

        // Если остаток 0, число уже кратно 5
        if (remainder == 0) {
            return minutes;
        }

        // Округляем вверх до ближайшего кратного 5
        return minutes + (5 - remainder);
    }
}
