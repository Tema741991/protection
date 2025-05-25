package ru.top.cinemas.mappers;

import jakarta.persistence.Column;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.top.cinemas.dtos.SessionCreateDto;
import ru.top.cinemas.entities.Film;
import ru.top.cinemas.entities.Hall;
import ru.top.cinemas.entities.Session;
import ru.top.cinemas.entities.SessionStatus;
import ru.top.cinemas.repositories.FilmRepository;
import ru.top.cinemas.repositories.HallRepository;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Data
@RequiredArgsConstructor
@Component
public class SessionCreateMapper {

    private final FilmRepository filmRepository;
    private final HallRepository hallRepository;

    public Session ToEntity(SessionCreateDto dto) {
        Session entity = new Session();
        if (dto.getId() != null) entity.setId(dto.getId());

        Film film = filmRepository.findById(dto.getFilmId())
                .orElseThrow(() -> new RuntimeException("Фильм не найден"));
        Hall hall = hallRepository.findById(dto.getHallId())
                .orElseThrow(() -> new RuntimeException("Зал не найден"));
        entity.setFilm(film);
        entity.setHall(hall);
        entity.setStartTime(roundUpToNearestFiveMinutes(dto.getStartTime()));
        LocalDateTime rawEndTime = entity.getStartTime().plusMinutes(film.getDuration());
        entity.setEndTime(roundUpToNearestFiveMinutes(rawEndTime));
        entity.setStatus(SessionStatus.DRAFT);
        return entity;
    }

    public SessionCreateDto ToDto(Session session) {
        SessionCreateDto dto = new SessionCreateDto();
        dto.setId(session.getId());
        dto.setFilmId(session.getFilm().getId());
        dto.setHallId(session.getHall().getId());
        dto.setStartTime(session.getStartTime());
        dto.setEndTime(session.getEndTime());
        dto.setStatus(session.getStatus());
        return dto;
    }

    private LocalDateTime roundUpToNearestFiveMinutes(LocalDateTime dateTime) {
        int minute = dateTime.getMinute();
        int remainder = minute % 5;

        if (remainder == 0) {
            return dateTime.withSecond(0).withNano(0);
        } else {
            int minutesToAdd = 5 - remainder;
            return dateTime.plusMinutes(minutesToAdd).withSecond(0).withNano(0);
        }
    }
}
