package ru.top.cinemas.dtos.main;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalTime;

@Data
@AllArgsConstructor
public class SessionInfoDto {
    private Long sessionId;
    private LocalTime startTime;
}
