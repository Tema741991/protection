package ru.top.cinemas.dtos;

import lombok.Data;
import ru.top.cinemas.entities.Hall;

import java.time.DayOfWeek;
import java.time.LocalTime;

@Data
public class WorkTimeDto {
    private Long id;
    private Hall hall;
    private DayOfWeek dayOfWeek;
    private LocalTime openTime;
    private LocalTime closeTime;
}
