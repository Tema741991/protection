package ru.top.cinemas.dtos.Schedule;

import lombok.Data;
import ru.top.cinemas.dtos.TechnicalBreakDto;

import java.util.List;

@Data
public class TimeGridResponse {
    private List<TimeSlotDto> slots;
    private List<TechnicalBreakDto> breaks;
    private List<SessionDto> sessions;
    private String hallName;
    private int stepMinutes;
}
