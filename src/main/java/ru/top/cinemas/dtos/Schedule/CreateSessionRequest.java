package ru.top.cinemas.dtos.Schedule;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class CreateSessionRequest {
    private Long filmId;
    private Long hallId;
    private LocalDate date;
    private LocalTime startTime;
}
