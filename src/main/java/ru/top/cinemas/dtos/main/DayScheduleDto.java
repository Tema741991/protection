package ru.top.cinemas.dtos.main;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
public class DayScheduleDto {
    private LocalDate date;
    private List<SessionInfoDto> sessions;
}
