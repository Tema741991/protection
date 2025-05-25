package ru.top.cinemas.mappers.schedule;

import lombok.Data;
import ru.top.cinemas.dtos.Schedule.SessionCreateDtoRest;
import ru.top.cinemas.entities.Session;

import java.time.temporal.ChronoUnit;

@Data
public class SessionCreateDtoRestMapper {
    public static SessionCreateDtoRest toDTO(Session session) {
        SessionCreateDtoRest dto = new SessionCreateDtoRest();
        dto.setId(session.getId());
        dto.setFilmTitle(session.getFilm().getTitle());
        dto.setStartTime(session.getStartTime());
        dto.setEndTime(session.getEndTime());
        dto.setDurationMinutes((int) ChronoUnit.MINUTES.between(
                session.getStartTime(), session.getEndTime()));
        return dto;
    }
}
