package ru.top.cinemas.dtos.Schedule;

import lombok.Data;

import java.time.LocalTime;

@Data
public class TimeSlotDto {
    private LocalTime startTime;
    private LocalTime endTime;
    private TimeSlotStatus status;
}
