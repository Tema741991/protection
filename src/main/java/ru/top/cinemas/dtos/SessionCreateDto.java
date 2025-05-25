package ru.top.cinemas.dtos;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import ru.top.cinemas.entities.SessionStatus;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class SessionCreateDto {
    private Long id;
    private Long hallId;
    private Long filmId;
    private LocalDate date;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private SessionStatus status;
}
