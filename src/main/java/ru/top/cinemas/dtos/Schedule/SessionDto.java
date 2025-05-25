package ru.top.cinemas.dtos.Schedule;

import lombok.Data;
import ru.top.cinemas.entities.SessionStatus;

import java.time.LocalDateTime;

@Data
public class SessionDto {
    private Long id;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private FilmSessionsDto film;
    private SessionStatus status;
}
