package ru.top.cinemas.dtos.Schedule;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SessionCreateDtoRest {
    private Long id;
    private Long filmId;
    private String filmTitle;
    private Long hallId;
    private String hallName;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Integer durationMinutes;
    private String status;
}
